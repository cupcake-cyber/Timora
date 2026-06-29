package com.timora.app.service;

import com.timora.app.dto.usersupplierpermission.UserPermissionMapDTO;
import com.timora.app.dto.usersupplierpermission.UserSupplierPermissionCreateDTO;
import com.timora.app.dto.usersupplierpermission.UserSupplierPermissionDTO;
import com.timora.app.model.enums.Permission;

import java.util.List;

public interface UserSupplierPermissionService {
    List<UserSupplierPermissionDTO> getByUserId(Long userId);
    List<UserSupplierPermissionDTO> getBySupplierId(Long supplierId);
    UserSupplierPermissionDTO create(UserSupplierPermissionCreateDTO dto);
    void delete(UserSupplierPermissionCreateDTO dto);
    UserPermissionMapDTO getPermissionMap(Long userId);
    boolean hasAnyPermission(Long userId);
}
