package com.timora.app.controllers;

import com.timora.app.models.Servicio;
import com.timora.app.service.ServicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/servicio")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    public ResponseEntity<List<Servicio>> getAll() {
        return ResponseEntity.ok(servicioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> getById(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.findById(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Servicio> getByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(servicioService.findByNombre(nombre));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Servicio>> getByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(servicioService.findByEstado(estado));
    }

    @PostMapping
    public ResponseEntity<Servicio> guardar(@RequestBody Servicio servicio) {
        Servicio creado = servicioService.guardar(servicio);
        return ResponseEntity.created(URI.create("/api/servicio/" + creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> actualizar(@PathVariable Long id, @RequestBody Servicio servicio) {
        return ResponseEntity.ok(servicioService.actualizar(id, servicio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Servicio> borrar(@PathVariable Long id) {
        servicioService.borrar(id);
        return ResponseEntity.noContent().build();
    }
}
