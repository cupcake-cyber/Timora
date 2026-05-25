package com.timora.app.service.impl;

import com.timora.app.model.RolePermission;
import com.timora.app.model.RolePermissionId;
import com.timora.app.repository.RolePermissionRepository;
import com.timora.app.service.RolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RolePermissionServiceImpl
        implements RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionServiceImpl(
            RolePermissionRepository rolePermissionRepository
    ) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public List<RolePermission> findAll() {

        return rolePermissionRepository.findAll();
    }

    @Override
    public RolePermission findById(RolePermissionId id) {

        return rolePermissionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "RolePermission not found"
                        )
                );
    }

    @Override
    public List<RolePermission> findByRoleId(Long roleId) {

        return rolePermissionRepository.findByRoleId(roleId);
    }

    @Override
    public List<RolePermission> findByPermissionId(Long permissionId) {

        return rolePermissionRepository.findByPermissionId(permissionId);
    }

    @Override
    public RolePermission save(
            RolePermission rolePermission
    ) {

        return rolePermissionRepository.save(rolePermission);
    }

    @Override
    public void delete(RolePermissionId id) {

        RolePermission existing =
                findById(id);

        rolePermissionRepository.delete(existing);
    }
}