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

        // =========================
        // PERMISOS
        // =========================
        if (!accessBase.isOwner(currentUser)) {
            if(!accessBase.isSameCompany(currentUser, baseCompanyId)){
                throw new BusinessException("You are not allowed to perform this operation.");
            }
            if (!accessBase.isAdmin(currentUser)) {
                if (!hasCustomer) {
                    throw new BusinessException("You are not allowed to perform this operation.");
                }
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

        Long baseCompanyId = current.getCompany().getId();

        boolean hasUser = current.getUser() != null;
        boolean hasCustomer = current.getCustomer() != null;
        boolean hasSupplier = current.getSupplier() != null;
        // =========================
        // ACCESS CONTROL
        // =========================
        if(!accessBase.isOwner(currentUser)){
            if(!accessBase.isSameCompany(currentUser, baseCompanyId)){
                throw new BusinessException("You are not allowed to perform this operation.");
            }
            if(!currentUser.getPersonId().equals(id)) {
                if(!accessBase.isAdmin(currentUser)) {
                    if(!hasCustomer){
                        throw new BusinessException("You are not allowed to perform this operation.");
                    }
                }
            }
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
            switch (request.getUser().getRole()){
                case OWNER->{
                    if(!accessBase.isOwner(currentUser)){
                        throw new BusinessException("You are not allowed to perform this operation.");
                    }
                }
                case ADMIN, USER -> {
                    if(!accessBase.isSameCompany(currentUser, baseCompanyId)){
                        throw new BusinessException("You are not allowed to perform this operation.");
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
        Person current = personService.findById(personId);
        Long baseCompanyId = current.getCompany().getId();
        boolean hasUser = current.getUser() != null;
        boolean hasCustomer = current.getCustomer() != null;
        boolean hasSupplier = current.getSupplier() != null;
        // =========================
        // ACCESS CONTROL
        // =========================
        if(!accessBase.isOwner(currentUser)){
            if(!accessBase.isSameCompany(currentUser, baseCompanyId)){
                throw new BusinessException("You are not allowed to perform this operation.");
            }
            if(!accessBase.isAdmin(currentUser)) {
                if(!hasCustomer){
                    throw new BusinessException("You are not allowed to perform this operation.");
                }
            }
        }

        // =========================
        // ELIMINACIÓN
        // =========================

        User user = current.getUser();
        personService.delete(personId);
        if (user != null) {
            userService.delete(user.getId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonIdentityDTO> getAll() {

        CurrentUser currentUser = securityHelper.getCurrentUser();
        List<Person> persons;
        switch (currentUser.getRole()) {
            case OWNER->{
                persons = personService.findAll();
            }
            case ADMIN-> {
                persons = personService.findByCompanyId(currentUser.getCompanyId());
            }
            case USER -> {
                    persons = personService.findByCompanyId(currentUser.getCompanyId());
//                Set<Long> personIds = new HashSet<>();
//
//                personIds.add(currentUser.getPersonId());
//
//                List<Supplier> accessibleSuppliers = supplierService.findByUserId(currentUser.getUserId());
//                boolean hasPermissions = !accessibleSuppliers.isEmpty();
//
//                List<Person> allPersonsInCompany = personService.findByCompanyId(currentUser.getCompanyId());
//                for (Person p : allPersonsInCompany) {
//                    if (p.getCustomer() != null) {
//                        personIds.add(p.getId());
//                    }
//                }
//                if (hasPermissions) {
//                    for (Supplier supplier : accessibleSuppliers) {
//                        personIds.add(supplier.getPerson().getId());
//                    }
//                }
//                if (personIds.isEmpty()) {
//                    return List.of();
//                }
//
//                persons = personService.findByIds(new ArrayList<>(personIds));
            }
            default -> {
                return List.of();
            }
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