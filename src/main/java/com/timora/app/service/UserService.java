package com.timora.app.service;

import com.timora.app.dto.CreatePersonRequest;

import com.timora.app.dto.security.CurrentUserDTO;
import com.timora.app.model.Person;
import com.timora.app.model.User;

public interface UserService {

    User findByLoginEmail(String email);

    User createUser(Person person, CreatePersonRequest.UserData data);

    User updateUser(User user, CreatePersonRequest.UserData data);

    void deleteUser(Long id);

    CurrentUserDTO buildCurrentUser(User user);
}