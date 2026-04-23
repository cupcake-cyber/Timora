package com.timora.app.controllers;

import com.timora.app.models.Transaccion;
import com.timora.app.service.TransaccionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
@CrossOrigin
public class TransaccionController {

    private final TransaccionService service;

    public TransaccionController(TransaccionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Transaccion> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Transaccion obtener(@PathVariable Long id) {
        return service.findById(id).orElse(null);
    }


    @PostMapping
    public Transaccion crear(@RequestBody Transaccion transaccion) {
        return service.guardar(transaccion);
    }

    @PutMapping("/{id}")
    public Transaccion actualizar(@PathVariable Long id, @RequestBody Transaccion transaccion) {
        return service.actualizar(id, transaccion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @GetMapping("/cita/{idCita}")
    public List<Transaccion> porCita(@PathVariable Long idCita) {
        return service.findByCita(idCita);
    }
}
