package com.timora.app.service.impl;

import com.timora.app.dto.payment.PaymentDTO;
import com.timora.app.dto.personidentity.PersonIdentityCreateDTO;
import com.timora.app.dto.personidentity.PersonIdentityDTO;
import com.timora.app.dto.customer.CustomerDTO;
import com.timora.app.dto.person.PersonDTO;
import com.timora.app.dto.personidentity.PersonIdentityPatchDTO;
import com.timora.app.dto.security.CurrentUser;
import com.timora.app.dto.supplier.SupplierDTO;
import com.timora.app.dto.user.UserDTO;
import com.timora.app.dto.user.UserPatchDTO;
import com.timora.app.exception.BusinessException;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.model.*;
import com.timora.app.model.enums.GlobalRole;
import com.timora.app.model.enums.PaymentStatus;
import com.timora.app.model.enums.Permission;
import com.timora.app.repository.BookingRepository;
import com.timora.app.repository.PaymentRepository;
import com.timora.app.security.AccessControlBaseService;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.*;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonManagementServiceImpl implements PersonManagementService {

    private final SecurityHelper securityHelper;
    private final AccessControlService access;
    private final AccessControlBaseService accessBase;

    private final PersonService personService;
    private final UserService userService;
    private final CustomerService customerService;
    private final SupplierService supplierService;


    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    private void validateSameCompany(Long baseCompanyId, Long entityCompanyId, String entityName) {

        if (entityCompanyId == null) return;

        if (!Objects.equals(baseCompanyId, entityCompanyId)) {
            throw new BusinessException(entityName + " must belong to the same company");
        }
    }

    @Override
    @Transactional
    public PersonIdentityDTO create(PersonIdentityCreateDTO request) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        if (request.getPerson() == null) {
            throw new BusinessException("You need to define a person.");
        }
        Long baseCompanyId = request.getPerson().getCompanyId();

        boolean hasUser = request.getUser() != null;
        boolean hasCustomer = request.getCustomer() != null;
        boolean hasSupplier = request.getSupplier() != null;

        if (!hasUser && !hasCustomer && !hasSupplier) {
            throw new BusinessException("A person must be created as a User, Supplier or Customer.");
        }
        if (hasCustomer && hasUser) {
            throw new BusinessException("A customer cannot be a user.");
        }
        if (hasCustomer && hasSupplier) {
            throw new BusinessException("A customer cannot be a supplier.");
        }

        validateSameCompany(baseCompanyId,
                request.getUser() != null ? request.getUser().getCompanyId() : null,
                "User");

        validateSameCompany(baseCompanyId,
                request.getSupplier() != null ? request.getSupplier().getCompanyId() : null,
                "Supplier");

        validateSameCompany(baseCompanyId,
                request.getCustomer() != null ? request.getCustomer().getCompanyId() : null,
                "Customer");

        // =========================
        // PERMISOS
        // =========================

        // OWNER: Puede hacer todo
        if (accessBase.isOwner(currentUser)) {
            // Pasa sin restricciones
        }
        // ADMIN: Puede hacer todo dentro de su compañía
        else if (accessBase.isAdmin(currentUser)) {
            // Verificar que sea de la misma compañía
            if (!currentUser.getCompanyId().equals(request.getPerson().getCompanyId())) {
                throw new ForbiddenException("You are not allowed to perform this action");
            }
            // ADMIN puede crear lo que sea dentro de su empresa
        }
        // USER: Solo puede crear CUSTOMER
        else {
            // Verificar que sea de la misma compañía
            if (!currentUser.getCompanyId().equals(request.getPerson().getCompanyId())) {
                throw new ForbiddenException("You are not allowed to perform this action");
            }

            // Un USER solo puede crear CUSTOMER (no USER ni SUPPLIER)
            if (!hasCustomer) {
                throw new ForbiddenException("You are not allowed to create Users or Suppliers");
            }

            // 🔥 Si el USER es SUPPLIER, puede crear customers sin permisos adicionales
            // Obtener el supplier del usuario actual
            Person currentPerson = personService.findById(currentUser.getPersonId());
            Supplier currentSupplier = currentPerson.getSupplier();

            if (currentSupplier != null) {
                // Es supplier, puede crear customers libremente (dentro de su compañía)
                // Pasa sin restricciones
            } else {
                // No es supplier, necesita permiso CUSTOMER_CREATE en algún proveedor
                access.requireCanCreateCustomerAnywhere(currentUser);
            }
        }

        // =========================
        // CREACIÓN
        // =========================

        Person person = personService.create(request.getPerson());

        User user = request.getUser() != null
                ? userService.create(person, request.getUser())
                : null;

        Customer customer = request.getCustomer() != null
                ? customerService.create(person, request.getCustomer())
                : null;

        Supplier supplier = request.getSupplier() != null
                ? supplierService.create(person, request.getSupplier())
                : null;

        return toDTO(person, user, customer, supplier);
    }

    @Override
    @Transactional
    public PersonIdentityDTO patch(Long id, PersonIdentityPatchDTO request) {

        CurrentUser currentUser = securityHelper.getCurrentUser();
        Person current = personService.findById(id);

        // =========================
        // 🔐 ACCESS CONTROL
        // =========================
        if (!accessBase.isOwner(currentUser)) {

            // Validar misma compañía
            if (!currentUser.getCompanyId().equals(current.getCompany().getId())) {
                throw new ForbiddenException("You are not allowed to perform this action");
            }

            // Si es USER
            if (!accessBase.isAdmin(currentUser)) {

                // Solo puede editar su propia persona
                if (!current.getId().equals(currentUser.getPersonId())) {
                    throw new ForbiddenException("You can only edit your own profile");
                }

                if (current.getCustomer() == null && current.getSupplier() == null) {
                    throw new ForbiddenException("You are not a customer or supplier");
                }

                // Solo puede editar CUSTOMER (no USER ni SUPPLIER)
                if (request.getUser() != null || request.getSupplier() != null) {
                    throw new ForbiddenException("You are not allowed to modify user or supplier data");
                }

                // 🔥 Si el USER es SUPPLIER, puede actualizar customers sin permisos adicionales
                if (request.getCustomer() != null) {
                    Person currentPerson = personService.findById(currentUser.getPersonId());
                    Supplier currentSupplier = currentPerson.getSupplier();

                    if (currentSupplier == null) {
                        // No es supplier, necesita permiso CUSTOMER_UPDATE en algún proveedor
                        access.requireCanUpdateCustomerAnywhere(currentUser);
                    }
                    // Si es supplier, pasa sin restricciones
                }
            }
        }

        // =========================
        // 🔒 BUSINESS RULES
        // =========================

        if (request.getCustomer() != null && current.getCustomer() == null) {
            throw new BusinessException("Customer cannot be added through this endpoint.");
        }

        if (request.getCustomer() != null && current.getCustomer() != null) {
            throw new BusinessException("Customer relationship cannot be modified.");
        }

        if (request.getUser() != null && current.getUser() == null && current.getSupplier() == null) {
            throw new BusinessException("Only supplier can be promoted to user.");
        }

        if (request.getUser() != null && request.getCustomer() != null) {
            throw new BusinessException("Customer cannot be a user.");
        }

        if (request.getSupplier() != null && request.getCustomer() != null) {
            throw new BusinessException("Customer cannot be a supplier.");
        }

        // =========================
        // PERSON PATCH
        // =========================
        Person person = personService.patch(id, request.getPerson());

        // =========================
        // USER PATCH + ROLES
        // =========================
        User user = null;

        if (request.getUser() != null) {

            if (person.getUser() == null) {
                throw new BusinessException("Person has no user to patch");
            }

            UserPatchDTO dto = request.getUser();

            if (!accessBase.isOwner(currentUser)) {

                if (accessBase.isUser(currentUser)) {
                    dto.setRole(GlobalRole.USER);
                }

                if (accessBase.isAdmin(currentUser)) {

                    if (!currentUser.getCompanyId().equals(person.getCompany().getId())) {
                        throw new ForbiddenException("Admin only within company");
                    }

                    if (dto.getRole() == GlobalRole.OWNER) {
                        throw new ForbiddenException("You are not allowed to assign OWNER");
                    }

                    // ADMIN solo USER → ADMIN
                    if (person.getUser().getRole() == GlobalRole.ADMIN &&
                            dto.getRole() == GlobalRole.USER) {
                        throw new ForbiddenException("Admin cannot downgrade ADMIN");
                    }
                }
            }

            user = userService.patch(person.getUser().getId(), dto);
        }

        // =========================
        // CUSTOMER PATCH
        // =========================
        Customer customer = null;
        if (request.getCustomer() != null) {

            if (person.getCustomer() == null) {
                throw new BusinessException("Person has no customer to patch");
            }

            customer = customerService.patch(person.getCustomer().getId(), request.getCustomer());
        }

        // =========================
        // SUPPLIER PATCH
        // =========================
        Supplier supplier = null;
        if (request.getSupplier() != null) {

            if (person.getSupplier() == null) {
                throw new BusinessException("Person has no supplier to patch");
            }

            supplier = supplierService.patch(person.getSupplier().getId(), request.getSupplier());
        }

        return toDTO(person, user, customer, supplier);
    }

    @Override
    @Transactional
    public void delete(Long personId) {

        CurrentUser currentUser = securityHelper.getCurrentUser();
        Person person = personService.findById(personId);

        // =========================
        // 🔐 ACCESS CONTROL
        // =========================

        // OWNER: Puede eliminar todo
        if (accessBase.isOwner(currentUser)) {
            // Pasa sin restricciones
        }
        // ADMIN: Puede eliminar todo dentro de su compañía
        else if (accessBase.isAdmin(currentUser)) {
            // Verificar que sea de la misma compañía
            if (!currentUser.getCompanyId().equals(person.getCompany().getId())) {
                throw new ForbiddenException("You are not allowed to perform this action");
            }
            // ADMIN puede eliminar lo que sea dentro de su empresa
        }
        // USER: Solo puede eliminar CUSTOMER
        else {
            // Verificar que sea de la misma compañía
            if (!currentUser.getCompanyId().equals(person.getCompany().getId())) {
                throw new ForbiddenException("You are not allowed to perform this action");
            }

            // Un USER solo puede eliminar CUSTOMER (no USER ni SUPPLIER)
            if (person.getCustomer() == null) {
                throw new ForbiddenException("You are not allowed to perform this action");
            }

            // Solo puede eliminar su propio perfil
            if (!person.getId().equals(currentUser.getPersonId())) {
                throw new ForbiddenException("You can only delete your own profile");
            }

            // 🔥 Si el USER es SUPPLIER, puede eliminar customers sin permisos adicionales
            Person currentPerson = personService.findById(currentUser.getPersonId());
            Supplier currentSupplier = currentPerson.getSupplier();

            if (currentSupplier == null) {
                // No es supplier, necesita permiso CUSTOMER_DELETE en algún proveedor
                access.requireCanDeleteCustomerAnywhere(currentUser);
            }
            // Si es supplier, pasa sin restricciones
        }

        // =========================
        // ELIMINACIÓN
        // =========================

        User user = person.getUser();
        personService.delete(personId);
        if (user != null) {
            userService.delete(user.getId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonIdentityDTO> getAll() {

        CurrentUser user = securityHelper.getCurrentUser();
        List<Person> persons;

        if (accessBase.isOwner(user)) {
            persons = personService.findAll();
        } else if (accessBase.isAdmin(user)) {
            persons = personService.findByCompanyId(user.getCompanyId());
        } else {
            // USER: Personas relacionadas con sus bookings + su propia persona
            Set<Long> personIds = new HashSet<>();

            // 🔥 CASO 1: Siempre incluir al propio usuario
            personIds.add(user.getPersonId());

            // 🔥 CASO 2: Obtener suppliers donde tiene permisos
            List<Supplier> accessibleSuppliers = supplierService.findByUserId(user.getUserId());

            if (!accessibleSuppliers.isEmpty()) {
                List<Long> supplierIds = accessibleSuppliers.stream()
                        .map(Supplier::getId)
                        .collect(Collectors.toList());

                // Obtener bookings de esos suppliers CON RELACIONES CARGADAS
                List<Booking> bookings = bookingRepository.findBySupplierIdsWithDetails(supplierIds);

                for (Booking booking : bookings) {
                    // Customer
                    if (booking.getCustomer() != null && booking.getCustomer().getPerson() != null) {
                        personIds.add(booking.getCustomer().getPerson().getId());
                    }
                    // Supplier del servicio
                    if (booking.getService() != null && booking.getService().getSupplier() != null) {
                        personIds.add(booking.getService().getSupplier().getPerson().getId());
                    }
                }
            }

            // 🔥 CASO 3: Si el usuario es supplier, agregar su propio supplier
            try {
                Person currentPerson = personService.findById(user.getPersonId());
                Supplier currentSupplier = currentPerson.getSupplier();
                if (currentSupplier != null) {
                    personIds.add(currentSupplier.getPerson().getId());
                }
            } catch (Exception e) {
                // Si falla, continuar
            }

            if (personIds.isEmpty()) {
                return List.of();
            }

            persons = personService.findByIds(new ArrayList<>(personIds));
        }

        return persons.stream()
                .map(p -> toDTO(p, p.getUser(), p.getCustomer(), p.getSupplier()))
                .toList();
    }

    @Override
    public PersonIdentityDTO getById(Long personId) {
        CurrentUser currentUser = securityHelper.getCurrentUser();
        Person person = personService.findById(personId);
        if (!accessBase.isOwner(currentUser)) {
            if (!currentUser.getCompanyId().equals(person.getCompany().getId())) {
                throw new ForbiddenException("You are not allowed to perform this action");
            }
        }

        User user = person.getUser();
        Customer customer = person.getCustomer();
        Supplier supplier = person.getSupplier();

        return toDTO(person, user, customer, supplier);

    }

    public PersonIdentityDTO toDTO(Person p, User u, Customer c, Supplier s) {

        PersonIdentityDTO dto = new PersonIdentityDTO();

        dto.setPerson(toPersonDTO(p));

        if (u != null) {
            dto.setUser(toUserDTO(u));
        }

        if (c != null) {
            dto.setCustomer(toCustomerDTO(c));
        }

        if (s != null) {
            dto.setSupplier(toSupplierDTO(s));
        }

        return dto;
    }

    private PersonDTO toPersonDTO(Person p) {
        PersonDTO dto = new PersonDTO();

        dto.setId(p.getId());
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setPhone(p.getPhone());
        dto.setEmail(p.getEmail());
        dto.setAddress(p.getAddress());
        dto.setCompanyId(p.getCompany().getId());
        dto.setStatus(p.getStatus());

        return dto;
    }

    private UserDTO toUserDTO(User u) {
        UserDTO dto = new UserDTO();

        dto.setId(u.getId());
        dto.setCompanyId(u.getCompany().getId());
        dto.setEmail(u.getEmail());
        dto.setRole(u.getRole());
        dto.setLastLoginAt(u.getLastLoginAt());
        dto.setCreatedDate(u.getCreatedAt());
        dto.setStatus(u.getStatus());

        return dto;
    }

    private CustomerDTO toCustomerDTO(Customer c) {
        CustomerDTO dto = new CustomerDTO();

        dto.setId(c.getId());
        dto.setCompanyId(c.getCompany().getId());
        dto.setPersonId(c.getPerson().getId());
        dto.setNotes(c.getNotes());
        dto.setCreatedAt(c.getCreatedAt());

        return dto;
    }

    private SupplierDTO toSupplierDTO(Supplier s) {
        SupplierDTO dto = new SupplierDTO();

        dto.setId(s.getId());
        dto.setCompanyId(s.getCompany().getId());
        dto.setPersonId(s.getPerson().getId());
        dto.setSpecialty(s.getSpecialty());
        dto.setNotes(s.getNotes());
        dto.setCreatedAt(s.getCreatedAt());

        return dto;
    }
}