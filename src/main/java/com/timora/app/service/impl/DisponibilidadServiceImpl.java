package com.timora.app.service.impl;

import com.timora.app.models.Availability;
import com.timora.app.repository.DisponibilidadRepository;
import com.timora.app.service.DisponibilidadService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del servicio de disponibilidades.
 */
@Service
public class DisponibilidadServiceImpl implements DisponibilidadService {

    private final DisponibilidadRepository disponibilidadRepository;

    public DisponibilidadServiceImpl(DisponibilidadRepository disponibilidadRepository) {
        this.disponibilidadRepository = disponibilidadRepository;
    }

    /**
     * Obtiene todas las disponibilidades registradas.
     *
     * @return lista de disponibilidades
     */
    @Override
    public List<Availability> findAll() {
        return disponibilidadRepository.findAll();
    }

    /**
     * Busca una disponibilidad por su ID.
     *
     * @param id identificador de la disponibilidad
     * @return disponibilidad encontrada
     * @throws RuntimeException si no existe
     */
    @Override
    public Availability findById(Long id) {
        return disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability no encontrada con id: " + id));
    }

    /**
     * Obtiene las disponibilidades de un proveedor.
     *
     * @param idProveedor identificador del proveedor
     * @return lista de disponibilidades del proveedor
     */
    @Override
    public List<Availability> findByProveedor(Long idProveedor) {
        return disponibilidadRepository.findByProveedorIdProveedor(idProveedor);
    }

    /**
     * Guarda una nueva disponibilidad en la base de datos.
     *
     * @param disponibilidad objeto disponibilidad a guardar
     * @return disponibilidad guardada
     */
    @Override
    public Availability guardar(Availability disponibilidad) {
        return disponibilidadRepository.save(disponibilidad);
    }

    /**
     * Actualiza una disponibilidad existente.
     *
     * @param id identificador de la disponibilidad
     * @param disponibilidad datos nuevos de la disponibilidad
     * @return disponibilidad actualizada
     */
    @Override
    public Availability actualizar(Long id, Availability disponibilidad) {
        Availability existente = disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability no encontrada con id: " + id));

        existente.setFechaInicio(disponibilidad.getFechaInicio());
        existente.setFechaFin(disponibilidad.getFechaFin());
        existente.setTipoRecurrencia(disponibilidad.getTipoRecurrencia());
        existente.setHoraInicio(disponibilidad.getHoraInicio());
        existente.setHoraFin(disponibilidad.getHoraFin());
        existente.setProveedor(disponibilidad.getProveedor());

        return disponibilidadRepository.save(existente);
    }

    /**
     * Elimina una disponibilidad por su ID.
     *
     * @param id identificador de la disponibilidad
     */
    @Override
    public void borrar(Long id) {
        disponibilidadRepository.deleteById(id);
    }
}