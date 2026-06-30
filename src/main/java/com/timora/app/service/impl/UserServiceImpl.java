package com.timora.app.service.impl;


import com.timora.app.dto.security.CurrentUser;
import com.timora.app.dto.user.UserCreateDTO;
import com.timora.app.dto.user.UserDTO;
import com.timora.app.dto.user.UserPatchDTO;
import com.timora.app.exception.BusinessException;
import com.timora.app.model.Person;
import com.timora.app.model.User;
import com.timora.app.model.enums.UserStatus;
import com.timora.app.repository.UserRepository;
import com.timora.app.service.ConfigurationService;
import com.timora.app.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfigurationService configurationService;

    @Override
    public User findById(Long id){
        return  userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public User create(Person person, UserCreateDTO userDTO) {

        User user = new User();

        user.setPerson(person);
        user.setCompany(person.getCompany());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setStatus(UserStatus.ACTIVE);
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        User saved  = userRepository.save(user);
        configurationService.create(user);

        return saved;
    }
    @Override
    @Transactional
    public User patch(Long id, UserPatchDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (dto.getEmail() != null) {

            if (!dto.getEmail().equals(user.getEmail())
                    && userRepository.existsByEmail(dto.getEmail())) {
                throw new BusinessException("Email already exists");
            }

            user.setEmail(dto.getEmail());
        }

        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    @Override
    public CurrentUser buildCurrentUser(User user) {

        CurrentUser dto = new CurrentUser();
        Person person = user.getPerson();

        dto.setCompanyId(user.getCompany().getId());

        dto.setPersonId(person.getId());
        dto.setUserId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCompanyId(user.getCompany().getId());

        return dto;
    }

    private UserDTO toDTO(User user) {

        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setCompanyId(user.getCompany().getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setLastLoginAt(user.getLastLoginAt());
        dto.setCreatedDate(user.getCreatedAt());
        dto.setStatus(user.getStatus());

        return dto;
    }
}