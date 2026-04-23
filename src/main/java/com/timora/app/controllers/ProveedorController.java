package com.timora.app.controllers;

import com.timora.app.models.Proveedor;
import com.timora.app.models.Usuario;
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
    public ResponseEntity<List<Proveedor>> getAll() {
        return ResponseEntity.ok(proveedorService.findAll());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Proveedor>> getActivos() {
        return ResponseEntity.ok(proveedorService.findActivos());
    }

    @PostMapping
    public ResponseEntity<Proveedor> crear(@RequestBody Proveedor proveedor) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(proveedorService.guardar(proveedor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> getById(@PathVariable Long id) {
        return ResponseEntity.ok(proveedorService.findById(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Proveedor> getByUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(proveedorService.findByUsuario(idUsuario));
    }
}