package com.timora.app.service;

import com.timora.app.models.Supplier;

import java.util.List;

public interface ProveedorService {

    List<Supplier> findAll();

    List<Supplier> findActivos();

    Supplier guardar(Supplier proveedor);

    Supplier findById(Long id);

    Supplier findByUsuario(Long idUsuario);
}