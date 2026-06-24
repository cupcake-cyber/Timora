package com.timora.app.service.impl;

import com.timora.app.dto.CreatePersonRequest;
import com.timora.app.dto.CurrentUserDTO;
import com.timora.app.model.Person;
import com.timora.app.model.User;
import com.timora.app.model.enums.GlobalRole;
import com.timora.app.model.enums.UserStatus;
import com.timora.app.repository.UserRepository;
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

    @Override
    public User findByLoginEmail(String email) {
        return userRepository.findByLoginEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User createUser(Person person, CreatePersonRequest.UserData data) {

        User user = new User();
        user.setPerson(person);
        user.setCompany(person.getCompany());
        user.setLoginEmail(data.getLoginEmail());
        user.setGlobalRole(GlobalRole.valueOf(data.getGlobalRole()));
        user.setStatus(UserStatus.ACTIVE);

        user.setPasswordHash(passwordEncoder.encode(data.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, CreatePersonRequest.UserData data) {

        if (data.getLoginEmail() != null) {
            user.setLoginEmail(data.getLoginEmail());
        }

        if (data.getGlobalRole() != null) {
            user.setGlobalRole(GlobalRole.valueOf(data.getGlobalRole()));
        }

        if (data.getPassword() != null) {
            user.setPasswordHash(passwordEncoder.encode(data.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public CurrentUserDTO buildCurrentUser(User user) {

        CurrentUserDTO dto = new CurrentUserDTO();

        dto.setId(user.getId());
        dto.setEmail(user.getLoginEmail());

        String fullName = user.getPerson() != null
                ? user.getPerson().getFirstName() + " " + user.getPerson().getLastName()
                : null;

        dto.setFullName(fullName);

        dto.setGlobalRole(user.getGlobalRole());
        dto.setActive(user.getStatus() != null && user.getStatus().name().equals("ACTIVE"));

        dto.setCompanyId(user.getCompany().getId());
        dto.setStatus(user.getStatus().name());

        // =========================
        // FLAGS (ajústalos a tu negocio real)
        // =========================

        dto.setCompanyAdmin(user.getGlobalRole().name().equals("OWNER")
                || user.getGlobalRole().name().equals("ADMIN"));

        dto.setSupplierUser(user.getPerson() != null && user.getPerson().getSupplier() != null);

        // supplierIds (si tienes relación real)
        if (user.getPerson() != null && user.getPerson().getSupplier() != null) {
            dto.setSupplierIds(List.of(user.getPerson().getSupplier().getId()));
        } else {
            dto.setSupplierIds(List.of());
        }

        // permisos (placeholder por ahora)
        dto.setSupplierPermissions(Map.of());

        return dto;
    }
}