package com.timora.app.service.impl;

import com.timora.app.models.Proveedor;
import com.timora.app.repository.ProveedorRepository;
import com.timora.app.service.ProveedorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor findById(Integer id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
    }

    @Override
    public Proveedor findByUsuario(Integer idUsuario) {
        return proveedorRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
    }
}