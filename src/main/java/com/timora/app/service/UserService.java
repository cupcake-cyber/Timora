package com.timora.app.service;

import com.timora.app.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    List<User> getAllUsers(Long companyId);
    User getUserById(Long id);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}
