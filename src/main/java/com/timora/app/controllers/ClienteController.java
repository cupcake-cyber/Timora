package com.timora.app.controllers;

import com.timora.app.models.Customer;
import com.timora.app.models.Supplier;
import com.timora.app.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Supplier>> getActivos() {
        return ResponseEntity.ok(clienteService.findActivos());
    }

    @PostMapping
    public ResponseEntity<Customer> crear(@RequestBody Customer cliente) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteService.guardar(cliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.findById(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Customer> getByUsuario(@PathVariable Long idUsuario) {
        clienteService.findAll()
        return ResponseEntity.ok(clienteService.findByUsuario(idUsuario));

    }
}