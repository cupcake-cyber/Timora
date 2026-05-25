package com.timora.app.service;

import com.timora.app.models.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
    List<Permission> findAll();

    Optional<Permission> findById(Long id);

    Permission save(Permission permission);

    Permission update(Long id, Permission permission);

    void delete(Long id);
}
