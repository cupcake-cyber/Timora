package com.timora.app.service;

import com.timora.app.dto.usersupplierpermission.UserPermissionMapDTO;
import com.timora.app.dto.usersupplierpermission.UserSupplierPermissionCreateDTO;
import com.timora.app.dto.usersupplierpermission.UserSupplierPermissionDTO;
import com.timora.app.model.enums.Permission;

import java.util.List;
import java.util.Set;

public interface UserSupplierPermissionService {
    UserSupplierPermissionDTO create(UserSupplierPermissionCreateDTO dto);
    void delete(UserSupplierPermissionCreateDTO dto);
    List<UserSupplierPermissionDTO> getByUserId(Long userId);
    List<UserSupplierPermissionDTO> getBySupplierId(Long supplierId);
    UserPermissionMapDTO getPermissionMap(Long userId);
    boolean hasAnyPermission(Long userId);
    boolean hasPermission(Long userId, Long supplierId, Permission permission);
    boolean hasAnyPermissionOnSupplier(Long userId, Long supplierId);
    Set<Permission> getPermissionsForSupplier(Long userId, Long supplierId);
    boolean hasAnyPermission(Long userId, Permission permission);
}