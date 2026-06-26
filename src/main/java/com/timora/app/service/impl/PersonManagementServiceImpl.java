package com.timora.app.service.impl;

import com.timora.app.dto.personidentity.PersonIdentityCreateDTO;
import com.timora.app.dto.personidentity.PersonIdentityDTO;
import com.timora.app.dto.customer.CustomerDTO;
import com.timora.app.dto.person.PersonDTO;
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
    private final PersonRepository personRepository;

    @Override
    @Transactional
    public PersonIdentityDTO create(PersonIdentityCreateDTO request) {

        User currentUser = securityHelper.getCurrentUser();

        // una persona no puede ser cliente ni proveedor al mismo tiempo
        if (request.getCustomer() != null && request.getSupplier() != null) {
            throw new BusinessException("A person cannot be both a Customer and a Supplier");
        }
        // un cliente no puede ser usuario
        if (request.getUser() != null && request.getCustomer() != null) {
            throw new BusinessException("A Customer cannot be a User");
        }

        //Se omite cualquier permiso de ser owner
        if (!auth.isOwner(currentUser)) {
            //Se comprueba que sean de la misma compañia
            if (!currentUser.getCompany().getId().equals(request.getPerson().getCompanyId())){
                throw new ForbiddenException("You are not allowed to perform this action");
            }else{
                //Se omite revisar que solo sea cliente si el user es admin
                if(!auth.isAdmin(currentUser)){
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
//    @Override
//    @Transactional
//    public PersonResponseDTO update(Long id, UpdatePersonRequest request) {
//
//        Person person = personRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Person not found"));
//
//        User currentUser = getCurrentUser();
//
//        authorizePersonAccess(currentUser, person);
//
//        if (request.getFirstName() != null)
//            person.setFirstName(request.getFirstName());
//
//        if (request.getLastName() != null)
//            person.setLastName(request.getLastName());
//
//        if (request.getPhone() != null)
//            person.setPhone(request.getPhone());
//
//        if (request.getEmail() != null)
//            person.setEmail(request.getEmail());
//
//        if (request.getAddress() != null)
//            person.setAddress(request.getAddress());
//
//        if (Boolean.TRUE.equals(request.getUpdateUser())) {
//
//            if (currentUser.getGlobalRole() == GlobalRole.USER) {
//                throw new ForbiddenException("USER cannot modify accounts");
//            }
//
//            if (request.getUser() != null) {
//
//                if (person.getUser() == null) {
//                    User user = userService.createUser(person, request.getUser());
//                    person.setUser(user);
//                } else {
//
//                    User user = person.getUser();
//
//                    if (request.getUser().getLoginEmail() != null) {
//                        user.setLoginEmail(request.getUser().getLoginEmail());
//                    }
//
//                    if (request.getUser().getGlobalRole() != null) {
//                        user.setGlobalRole(
//                                GlobalRole.valueOf(request.getUser().getGlobalRole())
//                        );
//                    }
//                }
//            }
//        }
//
//        return mapToDTO(personRepository.save(person));
//    }
//
//    @Override
//    @Transactional
//    public void delete(Long id) {
//
//        Person person = personRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Person not found"));
//
//        User currentUser = getCurrentUser();
//
//        authorizeDeleteAccess(currentUser, person);
//
//        person.setStatus(PersonStatus.INACTIVE);
//
//        if (person.getUser() != null) {
//            person.getUser().setStatus(UserStatus.INACTIVE);
//        }
//
//        personRepository.save(person);
//    }
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
        dto.setEmail(u.getLoginEmail());
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