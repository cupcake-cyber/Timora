package com.timora.app.service;

import com.timora.app.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();

    Optional<Role> findById(Long id);

    Role save(Role role);

    Role update(Long id, Role role);

    void delete(Long id);
}
