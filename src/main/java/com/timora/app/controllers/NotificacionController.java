package com.timora.app.controllers;

import com.timora.app.models.Notification;
import com.timora.app.service.NotificacionService;
import com.timora.app.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")

public class NotificacionController {

    private final NotificacionService notificacionService;
    private final UsuarioService usuarioService;

    public NotificacionController(NotificacionService notificacionService,
                                  UsuarioService usuarioService) {
        this.notificacionService = notificacionService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Notification> listar() {
        return notificacionService.findAll();
    }

    @GetMapping("/{id}")
    public Notification obtener(@PathVariable Long id) {
        return notificacionService.findById(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Notification> porUsuario(@PathVariable Long idUsuario) {
        Usuario usuario = usuarioService.findById(idUsuario);
        return notificacionService.findByUsuario(usuario);
    }

    @PostMapping
    public Notification crear(@RequestBody Notification notificacion) {
        return notificacionService.guardar(notificacion);
    }

    @PutMapping("/{id}")
    public Notification actualizar(@PathVariable Long id,
                                   @RequestBody Notification notificacion) {
        return notificacionService.actualizar(id, notificacion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        notificacionService.borrar(id);
    }

    @PutMapping("/{id}/leer")
    public Notification marcarComoLeida(@PathVariable Long id) {
        return notificacionService.marcarComoLeida(id);
    }
}