package com.timora.app.service;

import com.timora.app.models.Configuracion;

import java.util.List;

public interface ConfiguracionService {

    List<Configuracion> findAll();

    Configuracion findById(Long id);

    Configuracion findByUsuarioId(Long idUsuario);

    Configuracion guardar(Configuracion configuracion);

    Configuracion actualizar(Long id, Configuracion configuracion);

    void borrar(Long id);
}