package com.timora.app.service;

import com.timora.app.models.Availability;

import java.util.List;

public interface DisponibilidadService {

    List<Availability> findAll();

    Availability findById(Long id);

    List<Availability> findByProveedor(Long idProveedor);

    Availability guardar(Availability disponibilidad);

    Availability actualizar(Long id, Availability disponibilidad);

    void borrar(Long id);
}