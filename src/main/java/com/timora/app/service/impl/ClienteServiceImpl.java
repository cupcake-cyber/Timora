package com.timora.app.service.impl;

import com.timora.app.models.Cliente;
import com.timora.app.models.Proveedor;
import com.timora.app.models.enums.EstadoUsuario;
import com.timora.app.repository.ClienteRepository;
import com.timora.app.service.ClienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de Cliente.
 * Contiene la lógica de negocio relacionada a clientes.
 */
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    /**
     * Constructor con inyección del repositorio de clientes.
     *
     * @param clienteRepository repositorio de acceso a datos de Cliente
     */
    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return lista de usuarios
     */
    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }
    /**
     * Obtiene todos los proveedores registrados activos.
     *
     * @return lista de usuarios
     */
    @Override
    public List<Proveedor> findActivos() {
        return clienteRepository.findByUsuario_Estado(EstadoUsuario.ACTIVO);
    }

    /**
     * Guarda un cliente en la base de datos.
     *
     * @param cliente objeto cliente a guardar
     * @return cliente guardado
     */
    @Override
    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    /**
     * Busca un cliente por su ID.
     *
     * @param id identificador del cliente
     * @return cliente encontrado
     * @throws IllegalArgumentException si no existe el cliente
     */
    @Override
    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    /**
     * Busca un cliente asociado a un usuario.
     *
     * @param idUsuario identificador del usuario
     * @return cliente asociado al usuario
     * @throws IllegalArgumentException si no existe el cliente
     */
    @Override
    public Cliente findByUsuario(Long idUsuario) {
        return clienteRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }
}