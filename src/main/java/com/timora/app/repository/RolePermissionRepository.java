package com.timora.app.repository;

import com.timora.app.model.RolePermission;
import com.timora.app.model.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolePermissionRepository
        extends JpaRepository<RolePermission, RolePermissionId> {

    List<RolePermission> findByRoleId(Long roleId);

    List<RolePermission> findByPermissionId(Long permissionId);

    Optional<RolePermission> findByRoleIdAndPermissionId(
            Long roleId,
            Long permissionId
    );
}