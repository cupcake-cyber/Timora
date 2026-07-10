package com.timora.app.repository;

import com.timora.app.model.UserSupplierPermission;
import com.timora.app.model.UserSupplierPermissionId;
import com.timora.app.model.enums.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface UserSupplierPermissionRepository extends JpaRepository<UserSupplierPermission, UserSupplierPermissionId> {
    List<UserSupplierPermission> findByUserId(Long userId);
    List<UserSupplierPermission> findBySupplierId(Long supplierId);
    boolean existsByUser_Id(Long userId);
    boolean existsByUser_IdAndSupplier_IdAndId_Permission(Long userId, Long supplierId, Permission permission);
    Set<Permission> findPermissionsByUser_IdAndSupplier_Id(Long userId, Long supplierId);
    @Query("SELECT p FROM UserSupplierPermission p WHERE p.user.id = :userId")
    List<UserSupplierPermission> findByUserIdWithSupplier(@Param("userId") Long userId);
    boolean existsByUser_IdAndSupplier_Id(Long userId, Long supplierId);
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM UserSupplierPermission p " +
            "WHERE p.user.id = :userId AND p.id.permission = :permission")
    boolean existsByUser_IdAndId_Permission(@Param("userId") Long userId, @Param("permission") Permission permission);
}