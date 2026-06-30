package com.timora.app.service;

import com.timora.app.dto.security.CurrentUser;
import com.timora.app.dto.user.UserCreateDTO;
import com.timora.app.dto.user.UserPatchDTO;
import com.timora.app.model.Person;
import com.timora.app.model.User;

public interface UserService {
    User create(Person person, UserCreateDTO userDTO);
    User findByEmail(String email);
    User findById(Long userId);
    User patch(Long id, UserPatchDTO dto);
    void delete(Long id);
    CurrentUser buildCurrentUser(User user);
}