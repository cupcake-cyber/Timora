package com.timora.app.controllers;

import com.timora.app.models.Transaccion;
import com.timora.app.service.TransaccionService;
import com.timora.app.service.impl.TransaccionServiceImpl;
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
    // GET ALL
    @GetMapping
    public List<Transaccion> listar() {
        return service.listarTodas();
    }
    // GET BY ID
    @GetMapping("/{id}")
    public Transaccion obtener(@PathVariable Long id) {
        return service.obtenerPorId(id).orElse(null);
    }

    // POST
    @PostMapping
    public Transaccion crear(@RequestBody Transaccion transaccion) {
        return service.guardar(transaccion);
    }

    // PUT
    @PutMapping("/{id}")
    public Transaccion actualizar(@PathVariable Long id, @RequestBody Transaccion transaccion) {
        return service.actualizar(id, transaccion);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
