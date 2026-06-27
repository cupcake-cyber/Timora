package com.timora.app.service;

import com.timora.app.dto.security.CurrentUserDTO;
import com.timora.app.dto.user.UserCreateDTO;
import com.timora.app.dto.user.UserDTO;
import com.timora.app.dto.user.UserPatchDTO;
import com.timora.app.model.Person;
import com.timora.app.model.User;

public interface UserService {
    User create(Person person, UserCreateDTO userDTO);
    User findByLoginEmail(String email);
    User findById(Long userId);
    User patch(Long id, UserPatchDTO dto);
    void delete(Long id);
    CurrentUserDTO buildCurrentUser(User user);
}