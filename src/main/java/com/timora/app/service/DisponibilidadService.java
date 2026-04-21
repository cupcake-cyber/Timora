package com.timora.app.service;

import com.timora.app.models.Disponibilidad;

import java.util.List;

public interface DisponibilidadService {

    List<Disponibilidad> findAll();

    Disponibilidad findById(Integer id);

    List<Disponibilidad> findByProveedor(Integer idProveedor);

    Disponibilidad guardar(Disponibilidad disponibilidad);

    Disponibilidad actualizar(Integer id, Disponibilidad disponibilidad);

    void borrar(Integer id);
}