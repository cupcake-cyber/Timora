package com.timora.app.controller;

import com.timora.app.model.RolePermission;
import com.timora.app.model.RolePermissionId;
import com.timora.app.service.RolePermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-permissions")
@CrossOrigin(origins = "*")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    public RolePermissionController(
            RolePermissionService rolePermissionService
    ) {
        this.rolePermissionService = rolePermissionService;
    }

    @GetMapping
    public ResponseEntity<List<RolePermission>> getAll() {

        List<RolePermission> rolePermissions =
                rolePermissionService.findAll();

        return ResponseEntity.ok(rolePermissions);
    }

    @GetMapping("/{roleId}/{permissionId}")
    public ResponseEntity<RolePermission> getById(
            @PathVariable Long roleId,
            @PathVariable Long permissionId
    ) {

        RolePermissionId id =
                new RolePermissionId(
                        roleId,
                        permissionId
                );

        RolePermission rolePermission =
                rolePermissionService.findById(id);

        return ResponseEntity.ok(rolePermission);
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<RolePermission>> getByRoleId(
            @PathVariable Long roleId
    ) {

        List<RolePermission> rolePermissions =
                rolePermissionService.findByRoleId(roleId);

        return ResponseEntity.ok(rolePermissions);
    }

    @GetMapping("/permission/{permissionId}")
    public ResponseEntity<List<RolePermission>> getByPermissionId(
            @PathVariable Long permissionId
    ) {

        List<RolePermission> rolePermissions =
                rolePermissionService.findByPermissionId(permissionId);

        return ResponseEntity.ok(rolePermissions);
    }

    @PostMapping
    public ResponseEntity<RolePermission> create(
            @RequestBody RolePermission rolePermission
    ) {

        RolePermission saved =
                rolePermissionService.save(rolePermission);

        return new ResponseEntity<>(
                saved,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{roleId}/{permissionId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long roleId,
            @PathVariable Long permissionId
    ) {

        RolePermissionId id =
                new RolePermissionId(
                        roleId,
                        permissionId
                );

        rolePermissionService.delete(id);

        return ResponseEntity.noContent().build();
    }
}