package com.timora.app.controllers;

import com.timora.app.models.Notificacion;
import com.timora.app.models.Usuario;
import com.timora.app.service.NotificacionService;
import com.timora.app.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final UsuarioService usuarioService;

    public NotificacionController(NotificacionService notificacionService,
                                  UsuarioService usuarioService) {
        this.notificacionService = notificacionService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Notificacion> listar() {
        return notificacionService.findAll();
    }

    @GetMapping("/{id}")
    public Notificacion obtener(@PathVariable Integer id) {
        return notificacionService.findById(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Notificacion> porUsuario(@PathVariable Integer idUsuario) {
        Usuario usuario = usuarioService.findById(idUsuario);
        return notificacionService.findByUsuario(usuario);
    }

    @PostMapping
    public Notificacion crear(@RequestBody Notificacion notificacion) {
        return notificacionService.guardar(notificacion);
    }

    @PutMapping("/{id}")
    public Notificacion actualizar(@PathVariable Integer id,
                                   @RequestBody Notificacion notificacion) {
        return notificacionService.actualizar(id, notificacion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        notificacionService.borrar(id);
    }
}