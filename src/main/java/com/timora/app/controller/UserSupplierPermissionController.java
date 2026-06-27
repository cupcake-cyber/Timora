package com.timora.app.controller;

import com.timora.app.dto.usersupplierpermission.UserSupplierPermissionCreateDTO;
import com.timora.app.dto.usersupplierpermission.UserSupplierPermissionDTO;
import com.timora.app.service.UserSupplierPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-supplier-permissions")
public class UserSupplierPermissionController {

    private final UserSupplierPermissionService userSupplierPermissionService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSupplierPermissionDTO>> getByUseId(@PathVariable Long userId) {
        return ResponseEntity.ok(userSupplierPermissionService.getByUserId(userId));
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<UserSupplierPermissionDTO>> getBySupplierId(@PathVariable Long supplierId) {
        return ResponseEntity.ok(userSupplierPermissionService.getBySupplierId(supplierId));
    }

    @PostMapping
    public ResponseEntity<UserSupplierPermissionDTO> create(
            @RequestBody UserSupplierPermissionCreateDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userSupplierPermissionService.create(dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestBody UserSupplierPermissionCreateDTO dto) {

        userSupplierPermissionService.delete(dto);
        return ResponseEntity.noContent().build();
    }
}