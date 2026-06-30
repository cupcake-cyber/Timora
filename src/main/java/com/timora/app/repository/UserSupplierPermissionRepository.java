package com.timora.app.repository;

import com.timora.app.model.UserSupplierPermission;
import com.timora.app.model.UserSupplierPermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSupplierPermissionRepository extends JpaRepository<UserSupplierPermission, UserSupplierPermissionId> {
    List<UserSupplierPermission> findByUserId(Long userId);
    List<UserSupplierPermission> findBySupplierId(Long supplierId);
    boolean existsByUser_Id(Long userId);
}