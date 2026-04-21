package com.timora.app.service.impl;

import com.timora.app.models.Proveedor;
import com.timora.app.repository.ProveedorRepository;
import com.timora.app.service.ProveedorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de Proveedor.
 * Contiene la lógica de negocio relacionada a proveedores.
 */
@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    /**
     * Constructor con inyección del repositorio de proveedores.
     *
     * @param proveedorRepository repositorio de acceso a datos de Proveedor
     */
    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Guarda un proveedor en la base de datos.
     *
     * @param proveedor objeto proveedor a guardar
     * @return proveedor guardado
     */
    @Override
    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    /**
     * Busca un proveedor por su ID.
     *
     * @param id identificador del proveedor
     * @return proveedor encontrado
     * @throws IllegalArgumentException si no existe el proveedor
     */
    @Override
    public Proveedor findById(Integer id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
    }

    /**
     * Busca un proveedor asociado a un usuario.
     *
     * @param idUsuario identificador del usuario
     * @return proveedor asociado al usuario
     * @throws IllegalArgumentException si no existe el proveedor
     */
    @Override
    public Proveedor findByUsuario(Integer idUsuario) {
        return proveedorRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
    }
}