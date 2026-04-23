package com.timora.app.service;

import com.timora.app.models.Cita;

import java.util.List;
import java.util.Optional;

public interface CitaService {

    List<Cita> findAll();
    Cita cancelar(Long id);
    Optional<Cita> findById(Long id);
    Cita confirmar(Long id);
    Cita guardar(Cita cita);
    List<Cita> findByProveedor(Long idProveedor);
    Cita actualizar(Long id, Cita cita);
    List<Cita> findByCliente(Long idCliente);
    void eliminar(Long id);
}
