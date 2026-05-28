package com.timora.app.repository;

import com.timora.app.model.RolePermission;
import com.timora.app.model.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RolePermissionRepository
        extends JpaRepository<RolePermission, RolePermissionId> {

    List<RolePermission> findByRoleId(Long roleId);

    List<RolePermission> findByPermissionId(Long permissionId);

    Optional<RolePermission> findByRoleIdAndPermissionId(
            Long roleId,
            Long permissionId
    );
    @Query("""
        SELECT p.code
        FROM UserSupplierRole usr
        JOIN usr.role r
        JOIN r.rolePermissions rp
        JOIN rp.permission p
        WHERE usr.user.id = :userId
        AND usr.supplier.id = :supplierId
    """)
    List<String> findPermissionCodesByUserAndSupplier(
            @Param("userId") Long userId,
            @Param("supplierId") Long supplierId
    );
}