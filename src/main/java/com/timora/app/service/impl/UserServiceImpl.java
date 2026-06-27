package com.timora.app.service.impl;


import com.timora.app.dto.security.CurrentUserDTO;
import com.timora.app.dto.user.UserCreateDTO;
import com.timora.app.dto.user.UserDTO;
import com.timora.app.exception.BusinessException;
import com.timora.app.model.Person;
import com.timora.app.model.Supplier;
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
    public User findByLoginEmail(String email) {
        return userRepository.findByLoginEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public User create(Person person, UserCreateDTO userDTO) {

        User user = new User();

        user.setPerson(person);
        user.setCompany(person.getCompany());
        user.setEmail(userDTO.getEmail());
        user.setGlobalRole(userDTO.getRole());
        user.setStatus(UserStatus.ACTIVE);
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));

        configurationService.create(user);

        return userRepository.save(user);
    }
    @Override
    @Transactional
    public User patch(Long id, UserDTO dto) {

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
            user.setGlobalRole(dto.getRole());
        }

        if (dto.getStatus() != null) {
            user.setStatus(dto.getStatus());
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