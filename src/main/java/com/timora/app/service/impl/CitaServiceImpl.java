package com.timora.app.service.impl;


import com.timora.app.models.Cita;
import com.timora.app.repository.CitaRepository;
import com.timora.app.service.CitaService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaServiceImpl implements CitaService {
    private final CitaRepository citaRepository;

    public CitaServiceImpl(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    @Override
    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }

    @Override
    public Optional<Cita> obtenerPorId(Integer id) {
        return citaRepository.findById(id);
    }

    @Override
    public Cita guardar(Cita cita) {
        cita.setFechaCreacion(LocalDateTime.now());
        return citaRepository.save(cita);
    }

    @Override
    public Cita actualizar(Integer id, Cita citaActualizada) {
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

    @Override
    public void eliminar(Integer id) {
        citaRepository.deleteById(id);
    }
}
