package com.timora.app.controllers;

import com.timora.app.models.Disponibilidad;
import com.timora.app.service.DisponibilidadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilidades")
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    public DisponibilidadController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    @GetMapping
    public ResponseEntity<List<Disponibilidad>> getAll() {
        return ResponseEntity.ok(disponibilidadService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disponibilidad> getById(@PathVariable Long id) {
        return ResponseEntity.ok(disponibilidadService.findById(id));
    }

    @GetMapping("/proveedor/{idProveedor}")
    public ResponseEntity<List<Disponibilidad>> getByProveedor(@PathVariable Long idProveedor) {
        return ResponseEntity.ok(disponibilidadService.findByProveedor(idProveedor));
    }

    @PostMapping
    public ResponseEntity<Disponibilidad> crear(@Valid @RequestBody Disponibilidad disponibilidad) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(disponibilidadService.guardar(disponibilidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Disponibilidad> actualizar(@PathVariable Long id,
                                                     @RequestBody Disponibilidad disponibilidad) {
        return ResponseEntity.ok(disponibilidadService.actualizar(id, disponibilidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        disponibilidadService.borrar(id);
        return ResponseEntity.noContent().build();
    }
}