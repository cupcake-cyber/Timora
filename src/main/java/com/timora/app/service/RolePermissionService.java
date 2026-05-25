package com.timora.app.service;

import com.timora.app.models.RolePermission;
import com.timora.app.models.RolePermissionId;

import java.util.List;

public interface RolePermissionService {

    List<RolePermission> findAll();

    RolePermission findById(RolePermissionId id);

    List<RolePermission> findByRoleId(Long roleId);

    List<RolePermission> findByPermissionId(Long permissionId);

    RolePermission save(RolePermission rolePermission);

    void delete(RolePermissionId id);
}