package com.timora.app.service.impl;

import com.timora.app.dto.CurrentUserDTO;
import com.timora.app.dto.UserSummaryDTO;
import com.timora.app.model.User;
import com.timora.app.model.enums.GlobalRole;
import com.timora.app.model.enums.UserStatus;
import com.timora.app.repository.PersonRepository;
import com.timora.app.repository.UserRepository;
import com.timora.app.repository.UserSupplierRoleRepository;
import com.timora.app.service.AuthorizationService;
import com.timora.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    private final AuthorizationService authorizationService;
    private final UserSupplierRoleRepository userSupplierRoleRepository;

    @Override
    public User createUser(User user){
        if (userRepository.existsByLoginEmail(user.getLoginEmail())) {
            throw new IllegalArgumentException("El email ya existe.");
        }

        user.setPasswordHash(
                passwordEncoder.encode(user.getPasswordHash())
        );

        user.setStatus(UserStatus.ACTIVE);
        user.setGlobalRole(GlobalRole.USER);
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummaryDTO> getAllUsers() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        User currentUser =
                findByEmail(authentication.getName());

        List<User> users;

        if (currentUser.getGlobalRole() == GlobalRole.OWNER) {

            users = userRepository.findAll();

        } else if (currentUser.getGlobalRole() == GlobalRole.ADMIN) {

            users = userRepository.findByCompanyId(
                    currentUser.getCompany().getId()
            );

        } else {

            throw new AccessDeniedException(
                    "No autorizado"
            );
        }

        return users.stream()
                .map(user -> {

                    UserSummaryDTO dto =
                            new UserSummaryDTO();

                    dto.setId(user.getId());
                    dto.setEmail(user.getLoginEmail());
                    dto.setGlobalRole(
                            user.getGlobalRole().name()
                    );

                    return dto;
                })
                .toList();
    }

    @Override
    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El id de usuario no existe."));
    }

    @Override
    public User updateUser(Long id, User updatedUser){
        User existingUser = getUserById(id);

        if (!existingUser.getLoginEmail().equals(updatedUser.getLoginEmail())
                && userRepository.existsByLoginEmail(updatedUser.getLoginEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        existingUser.setLoginEmail(updatedUser.getLoginEmail());

        if (updatedUser.getPasswordHash() != null &&
                !updatedUser.getPasswordHash().isBlank()) {
            existingUser.setPasswordHash(
                    passwordEncoder.encode(updatedUser.getPasswordHash())
            );
        }

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id){
        User user = getUserById(id);
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {

        return userRepository.findByEmailAndStatus(
                email,
                UserStatus.ACTIVE
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "Usuario no encontrado."
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CurrentUserDTO buildCurrentUser(User user) {

        CurrentUserDTO dto = new CurrentUserDTO();

        dto.setId(user.getId());

        dto.setEmail(user.getLoginEmail());

        dto.setGlobalRole(user.getGlobalRole());

        dto.setActive(
                user.getStatus() == UserStatus.ACTIVE
        );

        dto.setCompanyId(
                user.getCompany().getId()
        );

        personRepository.findByUserId(user.getId())
                .ifPresent(person -> {

                    String fullName =
                            person.getFirstName()
                                    + " "
                                    + person.getLastName();

                    dto.setFullName(fullName);
                });

        List<Long> supplierIds =
                userSupplierRoleRepository
                        .findSupplierIdsByUserId(
                                user.getId()
                        );

        dto.setSupplierIds(supplierIds);

        dto.setSupplierUser(
                !supplierIds.isEmpty()
        );

        dto.setCompanyAdmin(
                user.getGlobalRole() == GlobalRole.ADMIN
                        || user.getGlobalRole() == GlobalRole.OWNER
        );

        Map<Long, List<String>> supplierPermissions =
                new HashMap<>();

        for (Long supplierId : supplierIds) {

            List<String> permissions =
                    authorizationService.getPermissions(
                            user.getId(),
                            supplierId
                    );

            supplierPermissions.put(
                    supplierId,
                    permissions
            );
        }

        dto.setSupplierPermissions(
                supplierPermissions
        );

        return dto;
    }
}
