package com.timora.app.service.impl;

import com.timora.app.repository.RolePermissionRepository;
import com.timora.app.service.AuthorizationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizationServiceImpl
        implements AuthorizationService {

    private final RolePermissionRepository rolePermissionRepository;

    public AuthorizationServiceImpl(
            RolePermissionRepository rolePermissionRepository
    ) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public List<String> getPermissions(
            Long userId,
            Long supplierId
    ) {

        return rolePermissionRepository
                .findPermissionCodesByUserAndSupplier(
                        userId,
                        supplierId
                );
    }

    @Override
    public boolean hasPermission(
            Long userId,
            Long supplierId,
            String permission
    ) {

        return getPermissions(userId, supplierId)
                .contains(permission);
    }
}