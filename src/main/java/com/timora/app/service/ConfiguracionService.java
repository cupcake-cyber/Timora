package com.timora.app.service;

import com.timora.app.models.Configuracion;

import java.util.List;

public interface ConfiguracionService {

    List<Configuracion> findAll();

    Configuracion findById(Integer id);

    Configuracion findByUsuarioId(Integer idUsuario);

    Configuracion guardar(Configuracion configuracion);

    Configuracion actualizar(Integer id, Configuracion configuracion);

    void borrar(Integer id);
}