package com.timora.app.service;

import com.timora.app.dto.usersupplierpermission.UserSupplierPermissionCreateDTO;
import com.timora.app.dto.usersupplierpermission.UserSupplierPermissionDTO;

import java.util.List;

public interface UserSupplierPermissionService {
    List<UserSupplierPermissionDTO> getByUserId(Long userId);
    List<UserSupplierPermissionDTO> getBySupplierId(Long supplierId);
    UserSupplierPermissionDTO create(UserSupplierPermissionCreateDTO dto);
    void delete(UserSupplierPermissionCreateDTO dto);
}
