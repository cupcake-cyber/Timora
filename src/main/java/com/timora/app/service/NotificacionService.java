package com.timora.app.service;

import com.timora.app.models.Notificacion;
import com.timora.app.models.Usuario;

import java.util.List;

public interface NotificacionService {

    List<Notificacion> findAll();

    Notificacion findById(Long id);

    List<Notificacion> findByUsuario(Usuario usuario);

    Notificacion guardar(Notificacion notificacion);

    Notificacion actualizar(Long id, Notificacion notificacion);

    void borrar(Long id);
}