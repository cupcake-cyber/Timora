package com.timora.app.controllers;

import com.timora.app.models.Cita;
import com.timora.app.service.CitaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }


    @GetMapping
    public ResponseEntity<List<Cita>> listarTodas() {
        return ResponseEntity.ok(citaService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerPorId(@PathVariable Long id) {
        return citaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ POST - crear cita
    @PostMapping
    public ResponseEntity<Cita> crear(@RequestBody Cita cita) {
        Cita nueva = citaService.guardar(cita);
        return ResponseEntity.ok(nueva);
    }

    // ✅ PUT - actualizar cita
    @PutMapping("/{id}")
    public ResponseEntity<Cita> actualizar(@PathVariable Long id, @RequestBody Cita cita) {
        try {
            Cita actualizada = citaService.actualizar(id, cita);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ DELETE - eliminar cita
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        citaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<Cita>> porCliente(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.findByCliente(id));
    }
    @GetMapping("/proveedor/{id}")
    public ResponseEntity<List<Cita>> porProveedor(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.findByProveedor(id));
    }
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Cita> confirmar(@PathVariable Long id) {
        try {
            Cita confirmada = citaService.confirmar(id);
            return ResponseEntity.ok(confirmada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Cita> cancelar(@PathVariable Long id) {
        try {
            Cita cancelada = citaService.cancelar(id);
            return ResponseEntity.ok(cancelada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
