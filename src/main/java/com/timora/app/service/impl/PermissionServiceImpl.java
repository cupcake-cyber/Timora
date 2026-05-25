package com.timora.app.service.impl;

import com.timora.app.models.Permission;
import com.timora.app.repository.PermissionRepository;
import com.timora.app.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public Permission save(Permission permission) {

        if (permissionRepository.existsByCode(permission.getCode())) {
            throw new RuntimeException("Permission code already exists");
        }

        return permissionRepository.save(permission);
    }

    @Override
    public Permission update(Long id, Permission permission) {

        Permission existing = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        existing.setCode(permission.getCode());
        existing.setDescription(permission.getDescription());

        return permissionRepository.save(existing);
    }

    @Override
    public void delete(Long id) {

        Permission existing = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        permissionRepository.delete(existing);
    }

}
