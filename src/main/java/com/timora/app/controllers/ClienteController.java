package com.timora.app.controllers;

import com.timora.app.models.Cliente;
import com.timora.app.models.Proveedor;
import com.timora.app.models.Usuario;
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
    public ResponseEntity<List<Cliente>> getAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Proveedor>> getActivos() {
        return ResponseEntity.ok(clienteService.findActivos());
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteService.guardar(cliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.findById(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Cliente> getByUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(clienteService.findByUsuario(idUsuario));
    }
}