package com.timora.app.service;

import com.timora.app.models.Proveedor;

import java.util.List;

public interface ProveedorService {

    Proveedor guardar(Proveedor proveedor);

    Proveedor findById(Long id);

    Proveedor findByUsuario(Long idUsuario);
}