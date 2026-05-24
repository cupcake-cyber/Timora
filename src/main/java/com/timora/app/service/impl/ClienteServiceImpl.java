package com.timora.app.service.impl;

import com.timora.app.models.Customer;
import com.timora.app.models.Supplier;
import com.timora.app.models.enums.EstadoUsuario;
import com.timora.app.repository.ClienteRepository;
import com.timora.app.service.ClienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de Customer.
 * Contiene la lógica de negocio relacionada a clientes.
 */
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    /**
     * Constructor con inyección del repositorio de clientes.
     *
     * @param clienteRepository repositorio de acceso a datos de Customer
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
    public List<Customer> findAll() {
        return clienteRepository.findAll();
    }
    /**
     * Obtiene todos los proveedores registrados activos.
     *
     * @return lista de usuarios
     */
    @Override
    public List<Supplier> findActivos() {
        return clienteRepository.findByUsuario_Estado(EstadoUsuario.ACTIVO);
    }

    /**
     * Guarda un cliente en la base de datos.
     *
     * @param cliente objeto cliente a guardar
     * @return cliente guardado
     */
    @Override
    public Customer guardar(Customer cliente) {
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
    public Customer findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer no encontrado"));
    }

    /**
     * Busca un cliente asociado a un usuario.
     *
     * @param idUsuario identificador del usuario
     * @return cliente asociado al usuario
     * @throws IllegalArgumentException si no existe el cliente
     */
    @Override
    public Customer findByUsuario(Long idUsuario) {
        return clienteRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Customer no encontrado"));
    }
}