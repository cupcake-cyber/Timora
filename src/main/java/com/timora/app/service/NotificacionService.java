package com.timora.app.service;

import com.timora.app.models.Notification;

import java.util.List;

public interface NotificacionService {

    List<Notification> findAll();

    Notification findById(Long id);

    List<Notification> findByUsuario(Usuario usuario);

    Notification guardar(Notification notificacion);

    Notification actualizar(Long id, Notification notificacion);
    Notification marcarComoLeida(Long id);
    void borrar(Long id);
}