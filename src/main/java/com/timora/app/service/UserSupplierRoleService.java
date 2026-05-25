package com.timora.app.service;

import com.timora.app.model.UserSupplierRole;
import com.timora.app.model.UserSupplierRoleId;

import java.util.List;

public interface UserSupplierRoleService {

    List<UserSupplierRole> findAll();

    UserSupplierRole findById(UserSupplierRoleId id);

    List<UserSupplierRole> findByUserId(Long userId);

    List<UserSupplierRole> findBySupplierId(Long supplierId);

    List<UserSupplierRole> findByRoleId(Long roleId);

    UserSupplierRole save(UserSupplierRole userSupplierRole);

    void delete(UserSupplierRoleId id);
}