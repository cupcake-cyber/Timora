package com.timora.app.service;

import com.timora.app.models.Servicio;
import java.util.List;

public interface ServicioService {
    List<Servicio> findAll();
    Servicio findById(Long id);
    Servicio findByNombre(String nombre);
    List<Servicio> findByEstado(String estado);
    Servicio guardar(Servicio servicio);
    Servicio actualizar(Long id, Servicio servicio);
    void borrar(Long id);
}
