package com.timora.app.service.impl;

import com.timora.app.models.Supplier;
import com.timora.app.models.enums.EstadoUsuario;
import com.timora.app.repository.ProveedorRepository;
import com.timora.app.service.ProveedorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de Supplier.
 * Contiene la lógica de negocio relacionada a proveedores.
 */
@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    /**
     * Constructor con inyección del repositorio de proveedores.
     *
     * @param proveedorRepository repositorio de acceso a datos de Supplier
     */
    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Obtiene todos los proveedores registrados.
     *
     * @return lista de usuarios
     */
    @Override
    public List<Supplier> findAll() {
        return proveedorRepository.findAll();
    }
    /**
     * Obtiene todos los proveedores registrados activos.
     *
     * @return lista de usuarios
     */
    @Override
    public List<Supplier> findActivos() {
        return proveedorRepository.findByUsuario_Estado(EstadoUsuario.ACTIVO);
    }
    /**
     * Guarda un proveedor en la base de datos.
     *
     * @param proveedor objeto proveedor a guardar
     * @return proveedor guardado
     */
    @Override
    public Supplier guardar(Supplier proveedor) {
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
    public Supplier findById(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier no encontrado"));
    }

    /**
     * Busca un proveedor asociado a un usuario.
     *
     * @param idUsuario identificador del usuario
     * @return proveedor asociado al usuario
     * @throws IllegalArgumentException si no existe el proveedor
     */
    @Override
    public Supplier findByUsuario(Long idUsuario) {
        return proveedorRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Supplier no encontrado"));
    }
}