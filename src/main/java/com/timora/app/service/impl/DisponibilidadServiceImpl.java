package com.timora.app.service.impl;

import com.timora.app.models.Disponibilidad;
import com.timora.app.repository.DisponibilidadRepository;
import com.timora.app.service.DisponibilidadService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisponibilidadServiceImpl implements DisponibilidadService {

    private final DisponibilidadRepository disponibilidadRepository;

    public DisponibilidadServiceImpl(DisponibilidadRepository disponibilidadRepository) {
        this.disponibilidadRepository = disponibilidadRepository;
    }

    @Override
    public List<Disponibilidad> findAll() {
        return disponibilidadRepository.findAll();
    }

    @Override
    public Disponibilidad findById(Integer id) {
        return disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada con id: " + id));
    }

    @Override
    public List<Disponibilidad> findByProveedor(Integer idProveedor) {
        return disponibilidadRepository.findByProveedorIdProveedor(idProveedor);
    }

    @Override
    public Disponibilidad guardar(Disponibilidad disponibilidad) {
        return disponibilidadRepository.save(disponibilidad);
    }

    @Override
    public Disponibilidad actualizar(Integer id, Disponibilidad disponibilidad) {
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

    @Override
    public void borrar(Integer id) {
        disponibilidadRepository.deleteById(id);
    }
}