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

    // ✅ GET - listar todas
    @GetMapping
    public ResponseEntity<List<Cita>> listarTodas() {
        return ResponseEntity.ok(citaService.listarTodas());
    }

    // ✅ GET - obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerPorId(@PathVariable Integer id) {
        return citaService.obtenerPorId(id)
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
    public ResponseEntity<Cita> actualizar(@PathVariable Integer id, @RequestBody Cita cita) {
        try {
            Cita actualizada = citaService.actualizar(id, cita);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ DELETE - eliminar cita
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        citaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
