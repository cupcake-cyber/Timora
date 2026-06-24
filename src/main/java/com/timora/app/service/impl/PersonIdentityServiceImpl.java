package com.timora.app.service.impl;

import com.timora.app.dto.CreatePersonRequest;
import com.timora.app.dto.PersonResponseDTO;
import com.timora.app.dto.UpdatePersonRequest;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.exception.NotFoundException;
import com.timora.app.model.Person;
import com.timora.app.model.User;
import com.timora.app.model.enums.GlobalRole;
import com.timora.app.model.enums.PersonStatus;
import com.timora.app.model.enums.UserStatus;
import com.timora.app.repository.PersonRepository;
import com.timora.app.repository.UserRepository;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PersonIdentityServiceImpl implements PersonIdentityService {

    private final SecurityHelper securityHelper;
    private final AccessControlService auth;

    private final PersonService personService;
    private final UserService userService;
    private final CustomerService customerService;
    private final SupplierService supplierService;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PersonResponseDTO create(CreatePersonRequest request) {

        User user = securityHelper.getCurrentUser();



        if (!auth.isOwner(user)) {

            if (user.getGlobalRole() == GlobalRole.USER) {
                throw new ForbiddenException("USER cannot create persons");
            }

            if (user.getCompany() == null ||
                    !user.getCompany().getId().equals(request.getCompanyId())) {
                throw new ForbiddenException("ADMIN can only create persons in their own company");
            }
        }

        Person person = personService.createBasePerson(request);

        if (request.getUser() != null) {
            User user = userService.createUser(person, request.getUser());
            person.setUser(user);
        }

        if (request.getCustomer() != null) {
            customerService.createCustomer(person, request.getCustomer());
        }

        if (request.getSupplier() != null) {
            supplierService.createSupplier(person, request.getSupplier());
        }

        return mapToDTO(person);
    }

    @Override
    public List<PersonResponseDTO> getAll() {

        User user = securityHelper.getCurrentUser();

        boolean isOwner = auth.isOwner(user);

        List<Person> persons = isOwner
                ? personRepository.findAllByStatus(PersonStatus.ACTIVE)
                : personRepository.findAllByStatusAndCompanyId(PersonStatus.INACTIVE, user.getCompany().getId());

        return persons.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public PersonResponseDTO getById(Long id) {

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Person not found"));

        authorizePersonAccess(getCurrentUser(), person);

        return mapToDTO(person);
    }

    @Override
    @Transactional
    public PersonResponseDTO update(Long id, UpdatePersonRequest request) {

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Person not found"));

        User currentUser = getCurrentUser();

        authorizePersonAccess(currentUser, person);

        if (request.getFirstName() != null)
            person.setFirstName(request.getFirstName());

        if (request.getLastName() != null)
            person.setLastName(request.getLastName());

        if (request.getPhone() != null)
            person.setPhone(request.getPhone());

        if (request.getEmail() != null)
            person.setEmail(request.getEmail());

        if (request.getAddress() != null)
            person.setAddress(request.getAddress());

        if (Boolean.TRUE.equals(request.getUpdateUser())) {

            if (currentUser.getGlobalRole() == GlobalRole.USER) {
                throw new ForbiddenException("USER cannot modify accounts");
            }

            if (request.getUser() != null) {

                if (person.getUser() == null) {
                    User user = userService.createUser(person, request.getUser());
                    person.setUser(user);
                } else {

                    User user = person.getUser();

                    if (request.getUser().getLoginEmail() != null) {
                        user.setLoginEmail(request.getUser().getLoginEmail());
                    }

                    if (request.getUser().getGlobalRole() != null) {
                        user.setGlobalRole(
                                GlobalRole.valueOf(request.getUser().getGlobalRole())
                        );
                    }
                }
            }
        }

        return mapToDTO(personRepository.save(person));
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Person not found"));

        User currentUser = getCurrentUser();

        authorizeDeleteAccess(currentUser, person);

        person.setStatus(PersonStatus.INACTIVE);

        if (person.getUser() != null) {
            person.getUser().setStatus(UserStatus.INACTIVE);
        }

        personRepository.save(person);
    }

    private void authorizePersonAccess(User currentUser, Person target) {

        GlobalRole role = currentUser.getGlobalRole();

        if (role == GlobalRole.OWNER) return;

        if (role == GlobalRole.ADMIN) {

            if (!currentUser.getCompany().getId()
                    .equals(target.getCompany().getId())) {
                throw new ForbiddenException("Different company");
            }
            return;
        }

        if (role == GlobalRole.USER) {

            if (currentUser.getPerson() == null) {
                throw new ForbiddenException("No linked person");
            }

            if (!target.getId().equals(currentUser.getPerson().getId())) {
                throw new ForbiddenException("Self only");
            }
        }
    }

    private void authorizeDeleteAccess(User currentUser, Person target) {

        GlobalRole role = currentUser.getGlobalRole();

        if (role == GlobalRole.OWNER) return;

        if (role == GlobalRole.ADMIN) {

            if (!currentUser.getCompany().getId()
                    .equals(target.getCompany().getId())) {
                throw new ForbiddenException("Different company");
            }
            return;
        }

        if (role == GlobalRole.USER) {
            throw new ForbiddenException("USER cannot delete");
        }
    }

    private PersonResponseDTO mapToDTO(Person p) {

        PersonResponseDTO dto = new PersonResponseDTO();

        dto.setId(p.getId());
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setPhone(p.getPhone());
        dto.setEmail(p.getEmail());
        dto.setAddress(p.getAddress());
        dto.setStatus(p.getStatus().name());

        if (p.getUser() != null) {
            dto.setHasUser(true);

            PersonResponseDTO.UserDTO u = new PersonResponseDTO.UserDTO();
            u.setId(p.getUser().getId());
            u.setLoginEmail(p.getUser().getLoginEmail());
            u.setGlobalRole(p.getUser().getGlobalRole().name());
            u.setStatus(p.getUser().getStatus().name());

            dto.setUser(u);
        } else {
            dto.setHasUser(false);
        }

        return dto;
    }
}