package com.timora.app.service.impl;

import com.timora.app.models.Cita;
import com.timora.app.models.enums.EstadoCita;
import com.timora.app.repository.CitaRepository;
import com.timora.app.repository.ClienteRepository;
import com.timora.app.repository.ServicioRepository;
import com.timora.app.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de citas.
 */
@Service
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;

    public CitaServiceImpl(
            CitaRepository citaRepository,
            ClienteRepository clienteRepository,
            ServicioRepository servicioRepository
    ) {
        this.citaRepository = citaRepository;
        this.clienteRepository = clienteRepository;
        this.servicioRepository = servicioRepository;
    }

    /**
     * Obtiene todas las citas registradas.
     *
     * @return lista de citas
     */
    @Override
    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    /**
     * Busca una cita por su ID.
     *
     * @param id identificador de la cita
     * @return cita encontrada
     */
    @Override
    public Optional<Cita> findById(Long id) {
        return citaRepository.findById(id);
    }

    /**
     * Guarda una nueva cita en la base de datos.
     *
     * @param cita objeto cita a guardar
     * @return cita guardada
     */
    @Override
    public Cita guardar(Cita cita) {

        Long idCliente = cita.getCliente().getIdCliente();
        Long idServicio = cita.getServicio().getIdServicio();

        cita.setCliente(clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado")));

        cita.setServicio(servicioRepository.findById(idServicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado")));

        cita.setFechaCreacion(LocalDateTime.now());

        return citaRepository.save(cita);
    }

    /**
     * Actualiza una cita existente.
     *
     * @param id identificador de la cita
     * @param citaActualizada datos nuevos de la cita
     * @return cita actualizada
     * @throws RuntimeException si no existe
     */
    @Override
    public Cita actualizar(Long id, Cita citaActualizada) {
        return citaRepository.findById(id).map(cita -> {

            cita.setCliente(citaActualizada.getCliente());
            cita.setServicio(citaActualizada.getServicio());
            cita.setHoraInicio(citaActualizada.getHoraInicio());
            cita.setHoraFin(citaActualizada.getHoraFin());
            cita.setEstado(citaActualizada.getEstado());
            cita.setDescripcion(citaActualizada.getDescripcion());

            return citaRepository.save(cita);

        }).orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + id));
    }

    /**
     * Elimina una cita por su ID.
     *
     * @param id identificador de la cita
     */
    @Override
    public void eliminar(Long id) {
        citaRepository.deleteById(id);
    }

    /**
     * Obtiene las citas asociadas a un cliente.
     *
     * @param idCliente identificador del cliente
     * @return lista de citas del cliente
     */
    @Override
    public List<Cita> findByCliente(Long idCliente) {
        return citaRepository.findByClienteIdCliente(idCliente);
    }

    /**
     * Obtiene las citas asociadas a un proveedor.
     *
     * @param idProveedor identificador del proveedor
     * @return lista de citas del proveedor
     */
    @Override
    public List<Cita> findByProveedor(Long idProveedor) {
        return citaRepository.findByServicioProveedorIdProveedor(idProveedor);
    }

    /**
     * Confirma una cita.
     *
     * @param id identificador de la cita
     * @return cita confirmada
     * @throws RuntimeException si no existe
     */
    @Override
    public Cita confirmar(Long id) {
        return citaRepository.findById(id).map(cita -> {

            cita.setEstado(EstadoCita.CONFIRMADA);

            return citaRepository.save(cita);

        }).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    /**
     * Cancela una cita.
     *
     * @param id identificador de la cita
     * @return cita cancelada
     * @throws RuntimeException si no existe
     */
    @Override
    public Cita cancelar(Long id) {
        return citaRepository.findById(id).map(cita -> {

            cita.setEstado(EstadoCita.CANCELADA);

            return citaRepository.save(cita);

        }).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }
}