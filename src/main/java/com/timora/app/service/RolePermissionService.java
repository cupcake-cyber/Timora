package com.timora.app.service;

import com.timora.app.model.RolePermission;
import com.timora.app.model.RolePermissionId;

import java.util.List;

public interface RolePermissionService {

    List<RolePermission> findAll();

    RolePermission findById(RolePermissionId id);

    List<RolePermission> findByRoleId(Long roleId);

    List<RolePermission> findByPermissionId(Long permissionId);

    RolePermission save(RolePermission rolePermission);

    void delete(RolePermissionId id);
}