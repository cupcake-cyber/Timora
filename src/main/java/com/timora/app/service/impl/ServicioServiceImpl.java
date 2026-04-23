package com.timora.app.service.impl;

import com.timora.app.models.Servicio;
import com.timora.app.models.enums.EstadoServicio;
import com.timora.app.repository.ServicioRepository;
import com.timora.app.service.ServicioService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioServiceImpl(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> findAll() {
        return servicioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Servicio findById(Long id) {
        return servicioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Servicio con id: " + id + " no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Servicio findByNombre(String nombre) {
        return servicioRepository.findByNombre(nombre)
                .orElseThrow(() -> new IllegalArgumentException("Servicio con Nombre: " + nombre + " no encontrado"));
    }

    @Override
    public List<Servicio> findByEstado(EstadoServicio estado) {
        return servicioRepository.findByEstado(estado);
    }

    @Override
    public Servicio guardar(Servicio servicio) {
        servicio.setIdServicio(null);

        if (servicio.getEstado() == null) {
            servicio.setEstado(EstadoServicio.APROBADO);
        }

        return servicioRepository.save(servicio);
    }

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

    @Override
    public void borrar(Long id) {
        Servicio existente = findById(id);
        servicioRepository.delete(existente);
    }
}
