package com.timora.app.service;

import com.timora.app.models.Transaccion;

import java.util.List;
import java.util.Optional;

public interface TransaccionService {
    List<Transaccion> findAll();

    Optional<Transaccion> findById(Long id);

    Transaccion guardar(Transaccion transaccion);

    Transaccion actualizar(Long id, Transaccion transaccion);

    void eliminar(Long id);
}
