package com.timora.app.service.impl;


import com.timora.app.dto.security.CurrentUserDTO;
import com.timora.app.dto.user.UserCreateDTO;
import com.timora.app.dto.user.UserDTO;
import com.timora.app.exception.BusinessException;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.model.Person;
import com.timora.app.model.Supplier;
import com.timora.app.model.User;
import com.timora.app.model.enums.UserStatus;
import com.timora.app.repository.UserRepository;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityHelper securityHelper;
    private final AccessControlService auth;



    @Override
    public User create(Person person, UserCreateDTO userDTO) {

        User currentUser = securityHelper.getCurrentUser();

        if (!auth.isOwner(currentUser)) {
            if (!currentUser.getCompany().getId().equals(userDTO.getCompanyId())) {
                throw new ForbiddenException("You cannot create entities outside your company.");
            }
            if(!auth.isAdmin(currentUser)){
                throw new ForbiddenException("Only administrators can create users.");
            }
        }
        if (userRepository.existsByEmail(userDTO.getEmail())){
            throw new BusinessException("User already exists");
        }

        User user = new User();

        user.setPerson(person);
        user.setCompany(person.getCompany());
        user.setEmail(userDTO.getEmail());
        user.setGlobalRole(userDTO.getRole());
        user.setStatus(UserStatus.ACTIVE);
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User findByLoginEmail(String email) {
        return userRepository.findByLoginEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
//    @Override
//    public User updateUser(User user, CreatePersonRequest.UserData data) {
//
//        if (data.getLoginEmail() != null) {
//            user.setLoginEmail(data.getLoginEmail());
//        }
//
//        if (data.getGlobalRole() != null) {
//            user.setGlobalRole(GlobalRole.valueOf(data.getGlobalRole()));
//        }
//
//        if (data.getPassword() != null) {
//            user.setPasswordHash(passwordEncoder.encode(data.getPassword()));
//        }
//
//        return userRepository.save(user);
//    }
//
//    @Override
//    public void deleteUser(Long id) {
//        userRepository.deleteById(id);
//    }
//
    @Override
    public CurrentUserDTO buildCurrentUser(User user) {

        CurrentUserDTO dto = new CurrentUserDTO();
        Person person = user.getPerson();

        dto.setCompanyId(user.getCompany().getId());

        dto.setPersonId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setPhone(person.getPhone());
        dto.setAddress(person.getAddress());

        dto.setUserId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getGlobalRole());
        dto.setStatus(user.getStatus());
        dto.setLastLoginAt(user.getLastLoginAt());
        dto.setCompanyId(user.getCompany().getId());

        Supplier supplier = person.getSupplier();

        if(supplier!=null){
            dto.setSupplierId(supplier.getId());
            dto.setSpecialty(supplier.getSpecialty());
            dto.setNotes(supplier.getNotes());
        }


        return dto;
    }

    private UserDTO toDTO(User user) {

        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setCompanyId(user.getCompany().getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getGlobalRole());
        dto.setLastLoginAt(user.getLastLoginAt());
        dto.setCreatedDate(user.getCreatedAt());
        dto.setStatus(user.getStatus());

        return dto;
    }
}