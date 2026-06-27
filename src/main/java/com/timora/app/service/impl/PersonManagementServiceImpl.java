package com.timora.app.service.impl;

import com.timora.app.dto.personidentity.PersonIdentityCreateDTO;
import com.timora.app.dto.personidentity.PersonIdentityDTO;
import com.timora.app.dto.customer.CustomerDTO;
import com.timora.app.dto.person.PersonDTO;
import com.timora.app.dto.personidentity.PersonIdentityPatchDTO;
import com.timora.app.dto.supplier.SupplierDTO;
import com.timora.app.dto.user.UserDTO;
import com.timora.app.exception.BusinessException;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.model.Customer;
import com.timora.app.model.Person;
import com.timora.app.model.Supplier;
import com.timora.app.model.User;
import com.timora.app.repository.*;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.*;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PersonManagementServiceImpl implements PersonManagementService {

    private final SecurityHelper securityHelper;
    private final AccessControlService auth;

    private final PersonService personService;
    private final UserService userService;
    private final CustomerService customerService;
    private final SupplierService supplierService;

    @Override
    @Transactional
    public PersonIdentityDTO create(PersonIdentityCreateDTO request) {

        User currentUser = securityHelper.getCurrentUser();

        if (request.getPerson() == null) {
            throw new BusinessException("You need to define a person.");
        }

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


        //Se omite cualquier permiso de ser owner
        if (!auth.isOwner(currentUser)) {
            //Se comprueba que sean de la misma compañia
            if (!currentUser.getCompany().getId().equals(request.getPerson().getCompanyId())){
                throw new ForbiddenException("You are not allowed to perform this action");
            }else{
                //Se omite revisar que solo sea cliente si el user es admin
                if(!auth.isAdmin(currentUser)){
                    //si no es customer no pasa
                    //TODO: el user tiene mas permisos, revisar la implementacion con UserSupplierPermission
                    if(request.getCustomer() == null){
                        throw new ForbiddenException("You are not allowed to perform this action");
                    }
                }
            }
        }

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

        User currentUser = securityHelper.getCurrentUser();

        Person current = personService.findById(id);

        // Se omite cualquier permiso de ser owner
        if (!auth.isOwner(currentUser)) {
            // Se comprueba que sean de la misma compañia
            if (!currentUser.getCompany().getId().equals(current.getCompany().getId())) {
                throw new ForbiddenException("You are not allowed to perform this action");
            } else {
                // Se omite revisar que solo sea cliente si el user es admin
                if (!auth.isAdmin(currentUser)) {

                    // si no es customer no pasa
                    if (request.getCustomer() == null) {
                        throw new ForbiddenException("You are not allowed to perform this action");
                    }
                    // si no es el mismo no pasa
                    if (!current.getId().equals(currentUser.getId())) {
                        throw new ForbiddenException("You are not allowed to perform this action");
                    }
                }
            }
        }

        boolean hasUser = current.getUser() != null;
        boolean hasCustomer = current.getCustomer() != null;
        boolean hasSupplier = current.getSupplier() != null;

        boolean requestUser = request.getUser() != null;
        boolean requestCustomer = request.getCustomer() != null;
        boolean requestSupplier = request.getSupplier() != null;

        // Un Customer no puede dejar de ser Customer ni convertirse en otro tipo.
        if (hasCustomer != requestCustomer) {
            throw new BusinessException("The customer relationship cannot be modified through this endpoint.");
        }

        // Nunca se puede quitar un User.
        if (hasUser && !requestUser) {
            throw new BusinessException("A user cannot be removed.");
        }

        // Solo un Supplier puede convertirse en User.
        if (!hasUser && requestUser && !hasSupplier) {
            throw new BusinessException("Only a supplier can be promoted to a user.");
        }
        // Customer nunca puede coexistir con User.
        if (requestCustomer && requestUser) {
            throw new BusinessException("A customer cannot be a user.");
        }

        // Customer nunca puede coexistir con Supplier.
        if (requestCustomer && requestSupplier) {
            throw new BusinessException("A customer cannot be a supplier.");
        }

        Person person = personService.patch(id, request.getPerson());

        User user = null;
        if (request.getUser() != null) {
            user = userService.patch(person.getUser().getId(), request.getUser());
        }

        Customer customer = null;
        if (request.getCustomer() != null) {
            customer = customerService.patch(person.getCustomer().getId(), request.getCustomer());
        }

        Supplier supplier = null;
        if (request.getSupplier() != null) {
            supplier = supplierService.patch(person.getSupplier().getId(), request.getSupplier());
        }

        return toDTO(person, user, customer, supplier);
    }

    @Override
    @Transactional
    public void delete(Long personId) {

        User currentUser = securityHelper.getCurrentUser();

        Person person = personService.findById(personId);

        //Se omite cualquier permiso de ser owner
        if (!auth.isOwner(currentUser)) {
            //Se comprueba que sean de la misma compañia
            if (!currentUser.getCompany().getId().equals(person.getCompany().getId())) {
                throw new ForbiddenException("You are not allowed to perform this action");
            }else{
                //Se omite revisar que solo sea cliente si el user es admin
                if(!auth.isAdmin(currentUser)){
                    //si no es customer no pasa
                    //TODO: el user tiene mas permisos, revisar la implementacion con UserSupplierPermission
                    if(person.getCustomer() == null){
                        throw new ForbiddenException("You are not allowed to perform this action");
                    }
                }
            }
        }


        User user = person.getUser();
        personService.delete(personId);
        if (user != null) {
            userService.delete(user.getId());
        }
    }
//    @Override
//    public List<PersonResponseDTO> getAll() {
//
//        User user = securityHelper.getCurrentUser();
//
//        boolean isOwner = auth.isOwner(user);
//
//        List<Person> persons = isOwner
//                ? personRepository.findAllByStatus(PersonStatus.ACTIVE)
//                : personRepository.findAllByStatusAndCompanyId(PersonStatus.INACTIVE, user.getCompany().getId());
//
//        return persons.stream()
//                .map(this::mapToDTO)
//                .toList();
//    }
//
//    @Override
//    public PersonResponseDTO getById(Long id) {
//
//        Person person = personRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Person not found"));
//
//        authorizePersonAccess(getCurrentUser(), person);
//
//        return mapToDTO(person);
//    }
//
//
//    private void authorizePersonAccess(User currentUser, Person target) {
//
//        GlobalRole role = currentUser.getGlobalRole();
//
//        if (role == GlobalRole.OWNER) return;
//
//        if (role == GlobalRole.ADMIN) {
//
//            if (!currentUser.getCompany().getId()
//                    .equals(target.getCompany().getId())) {
//                throw new ForbiddenException("Different company");
//            }
//            return;
//        }
//
//        if (role == GlobalRole.USER) {
//
//            if (currentUser.getPerson() == null) {
//                throw new ForbiddenException("No linked person");
//            }
//
//            if (!target.getId().equals(currentUser.getPerson().getId())) {
//                throw new ForbiddenException("Self only");
//            }
//        }
//    }
//
//    private void authorizeDeleteAccess(User currentUser, Person target) {
//
//        GlobalRole role = currentUser.getGlobalRole();
//
//        if (role == GlobalRole.OWNER) return;
//
//        if (role == GlobalRole.ADMIN) {
//
//            if (!currentUser.getCompany().getId()
//                    .equals(target.getCompany().getId())) {
//                throw new ForbiddenException("Different company");
//            }
//            return;
//        }
//
//        if (role == GlobalRole.USER) {
//            throw new ForbiddenException("USER cannot delete");
//        }
//    }

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
        dto.setRole(u.getGlobalRole());
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