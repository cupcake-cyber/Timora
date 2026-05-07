package com.timora.app.service.impl;

import com.timora.app.models.Servicio;
import com.timora.app.models.enums.EstadoServicio;
import com.timora.app.repository.ServicioRepository;
import com.timora.app.service.ServicioService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del servicio de servicios.
 */
@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioServiceImpl(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    /**
     * Obtiene todos los servicios registrados.
     *
     * @return lista de servicios
     */
    @Override
    @Transactional(readOnly = true)
    public List<Servicio> findAll() {
        return servicioRepository.findAll();
    }

    /**
     * Busca un servicio por su ID.
     *
     * @param id identificador del servicio
     * @return servicio encontrado
     * @throws IllegalArgumentException si no existe
     */
    @Override
    @Transactional(readOnly = true)
    public Servicio findById(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Servicio con id: " + id + " no encontrado"));
    }

    /**
     * Busca un servicio por su nombre.
     *
     * @param nombre nombre del servicio
     * @return servicio encontrado
     * @throws IllegalArgumentException si no existe
     */
    @Override
    @Transactional(readOnly = true)
    public Servicio findByNombre(String nombre) {
        return servicioRepository.findByNombre(nombre)
                .orElseThrow(() -> new IllegalArgumentException("Servicio con Nombre: " + nombre + " no encontrado"));
    }

    /**
     * Obtiene todos los servicios según su estado.
     *
     * @param estado estado del servicio
     * @return lista de servicios filtrados
     */
    @Override
    public List<Servicio> findByEstado(EstadoServicio estado) {
        return servicioRepository.findByEstado(estado);
    }

    /**
     * Guarda un nuevo servicio en la base de datos.
     *
     * @param servicio objeto servicio a guardar
     * @return servicio guardado
     */
    @Override
    public Servicio guardar(Servicio servicio) {
        servicio.setIdServicio(null);

        if (servicio.getEstado() == null) {
            servicio.setEstado(EstadoServicio.APROBADO);
        }

        return servicioRepository.save(servicio);
    }

    /**
     * Actualiza un servicio existente.
     *
     * @param id identificador del servicio
     * @param servicio datos nuevos del servicio
     * @return servicio actualizado
     */
    @Override
    public Servicio actualizar(Long id, Servicio servicio) {
        Servicio existente = findById(id);

        existente.setProveedor(servicio.getProveedor());
        existente.setNombre(servicio.getNombre());
        existente.setDescripcion(servicio.getDescripcion());
        existente.setDuracion(servicio.getDuracion());
        existente.setPrecio(servicio.getPrecio());
        existente.setEstado(servicio.getEstado());

        return servicioRepository.save(existente);
    }

    /**
     * Elimina un servicio por su ID.
     *
     * @param id identificador del servicio
     */
    @Override
    public void borrar(Long id) {
        Servicio existente = findById(id);
        servicioRepository.delete(existente);
    }
}