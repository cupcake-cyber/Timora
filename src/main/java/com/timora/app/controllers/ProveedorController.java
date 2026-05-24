package com.timora.app.controllers;

import com.timora.app.models.Supplier;
import com.timora.app.service.ProveedorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getAll() {
        return ResponseEntity.ok(proveedorService.findAll());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Supplier>> getActivos() {
        return ResponseEntity.ok(proveedorService.findActivos());
    }

    @PostMapping
    public ResponseEntity<Supplier> crear(@RequestBody Supplier proveedor) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(proveedorService.guardar(proveedor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getById(@PathVariable Long id) {
        return ResponseEntity.ok(proveedorService.findById(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Supplier> getByUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(proveedorService.findByUsuario(idUsuario));
    }
}