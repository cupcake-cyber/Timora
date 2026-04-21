package com.timora.app.service;

import com.timora.app.models.Disponibilidad;

import java.util.List;

public interface DisponibilidadService {

    List<Disponibilidad> findAll();

    Disponibilidad findById(Long id);

    List<Disponibilidad> findByProveedor(Long idProveedor);

    Disponibilidad guardar(Disponibilidad disponibilidad);

    Disponibilidad actualizar(Long id, Disponibilidad disponibilidad);

    void borrar(Long id);
}