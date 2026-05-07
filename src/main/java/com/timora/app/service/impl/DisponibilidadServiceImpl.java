package com.timora.app.service.impl;

import com.timora.app.models.Disponibilidad;
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
    public List<Disponibilidad> findAll() {
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
    public Disponibilidad findById(Long id) {
        return disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada con id: " + id));
    }

    /**
     * Obtiene las disponibilidades de un proveedor.
     *
     * @param idProveedor identificador del proveedor
     * @return lista de disponibilidades del proveedor
     */
    @Override
    public List<Disponibilidad> findByProveedor(Long idProveedor) {
        return disponibilidadRepository.findByProveedorIdProveedor(idProveedor);
    }

    /**
     * Guarda una nueva disponibilidad en la base de datos.
     *
     * @param disponibilidad objeto disponibilidad a guardar
     * @return disponibilidad guardada
     */
    @Override
    public Disponibilidad guardar(Disponibilidad disponibilidad) {
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
    public Disponibilidad actualizar(Long id, Disponibilidad disponibilidad) {
        Disponibilidad existente = disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada con id: " + id));

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