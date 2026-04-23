package com.timora.app.service;

import com.timora.app.models.Cita;

import java.util.List;
import java.util.Optional;

public interface CitaService {

    List<Cita> findAll();

    Optional<Cita> findById(Integer id);

    Cita guardar(Cita cita);

    Cita actualizar(Integer id, Cita cita);

    void eliminar(Integer id);
}
