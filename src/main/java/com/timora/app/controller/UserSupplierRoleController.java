package com.timora.app.controller;

import com.timora.app.model.UserSupplierRole;
import com.timora.app.model.UserSupplierRoleId;
import com.timora.app.service.UserSupplierRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-supplier-roles")
@CrossOrigin(origins = "*")
public class UserSupplierRoleController {

    private final UserSupplierRoleService userSupplierRoleService;

    public UserSupplierRoleController(
            UserSupplierRoleService userSupplierRoleService
    ) {
        this.userSupplierRoleService = userSupplierRoleService;
    }

    @GetMapping
    public ResponseEntity<List<UserSupplierRole>> getAll() {

        List<UserSupplierRole> userSupplierRoles =
                userSupplierRoleService.findAll();

        return ResponseEntity.ok(userSupplierRoles);
    }

    @GetMapping("/{userId}/{supplierId}/{roleId}")
    public ResponseEntity<UserSupplierRole> getById(
            @PathVariable Long userId,
            @PathVariable Long supplierId,
            @PathVariable Long roleId
    ) {

        UserSupplierRoleId id =
                new UserSupplierRoleId(
                        userId,
                        supplierId,
                        roleId
                );

        UserSupplierRole userSupplierRole =
                userSupplierRoleService.findById(id);

        return ResponseEntity.ok(userSupplierRole);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSupplierRole>> getByUserId(
            @PathVariable Long userId
    ) {

        List<UserSupplierRole> userSupplierRoles =
                userSupplierRoleService.findByUserId(userId);

        return ResponseEntity.ok(userSupplierRoles);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<UserSupplierRole>> getBySupplierId(
            @PathVariable Long supplierId
    ) {

        List<UserSupplierRole> userSupplierRoles =
                userSupplierRoleService.findBySupplierId(supplierId);

        return ResponseEntity.ok(userSupplierRoles);
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<UserSupplierRole>> getByRoleId(
            @PathVariable Long roleId
    ) {

        List<UserSupplierRole> userSupplierRoles =
                userSupplierRoleService.findByRoleId(roleId);

        return ResponseEntity.ok(userSupplierRoles);
    }

    @PostMapping
    public ResponseEntity<UserSupplierRole> create(
            @RequestBody UserSupplierRole userSupplierRole
    ) {

        UserSupplierRole saved =
                userSupplierRoleService.save(userSupplierRole);

        return new ResponseEntity<>(
                saved,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{userId}/{supplierId}/{roleId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long userId,
            @PathVariable Long supplierId,
            @PathVariable Long roleId
    ) {

        UserSupplierRoleId id =
                new UserSupplierRoleId(
                        userId,
                        supplierId,
                        roleId
                );

        userSupplierRoleService.delete(id);

        return ResponseEntity.noContent().build();
    }
}