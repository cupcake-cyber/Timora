package com.timora.app.service.impl;

import com.timora.app.model.User;
import com.timora.app.model.enums.GlobalRole;
import com.timora.app.model.enums.UserStatus;
import com.timora.app.repository.UserRepository;
import com.timora.app.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
    public List<User> getAllUsers(Long companyId) {
        return userRepository.findByCompanyId(companyId);
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
}
