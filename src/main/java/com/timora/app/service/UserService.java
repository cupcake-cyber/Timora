package com.timora.app.service;

import com.timora.app.dto.CurrentUserDTO;
import com.timora.app.dto.UserSummaryDTO;
import com.timora.app.model.User;
import java.util.List;

public interface UserService {
    User createUser(User user);
    List<UserSummaryDTO>getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
    User findByEmail(String email);
    CurrentUserDTO buildCurrentUser(User user);
}