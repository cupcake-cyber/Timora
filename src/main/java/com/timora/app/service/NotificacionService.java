package com.timora.app.service;

import com.timora.app.models.Notificacion;
import com.timora.app.models.Usuario;

import java.util.List;

public interface NotificacionService {

    List<Notificacion> findAll();

    Notificacion findById(Integer id);

    List<Notificacion> findByUsuario(Usuario usuario);

    Notificacion guardar(Notificacion notificacion);

    Notificacion actualizar(Integer id, Notificacion notificacion);

    void borrar(Integer id);
}