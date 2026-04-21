package com.timora.app.controllers;

import com.timora.app.models.Configuracion;
import com.timora.app.service.ConfiguracionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configuraciones")
@CrossOrigin(origins = "*")
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    public ConfiguracionController(ConfiguracionService configuracionService) {
        this.configuracionService = configuracionService;
    }

    @GetMapping
    public ResponseEntity<List<Configuracion>> getAll() {
        return ResponseEntity.ok(configuracionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Configuracion> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(configuracionService.findById(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Configuracion> getByUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(configuracionService.findByUsuarioId(idUsuario));
    }

    @PostMapping
    public ResponseEntity<Configuracion> crear(@RequestBody Configuracion configuracion) {
        return new ResponseEntity<>(configuracionService.guardar(configuracion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Configuracion> actualizar(@PathVariable Integer id,
                                                    @RequestBody Configuracion configuracion) {
        return ResponseEntity.ok(configuracionService.actualizar(id, configuracion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Integer id) {
        configuracionService.borrar(id);
        return ResponseEntity.noContent().build();
    }
}