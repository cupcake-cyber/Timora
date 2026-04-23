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

    @Override
    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    @Override
    public Optional<Cita> findById(Long id) {
        return citaRepository.findById(id);
    }

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

    @Override
    public void eliminar(Long id) {
        citaRepository.deleteById(id);
    }

    @Override
    public List<Cita> findByCliente(Long idCliente) {
        return citaRepository.findByClienteIdCliente(idCliente);
    }

    @Override
    public List<Cita> findByProveedor(Long idProveedor) {
        return citaRepository.findByServicioProveedorIdProveedor(idProveedor);
    }

    @Override
    public Cita confirmar(Long id) {
        return citaRepository.findById(id).map(cita -> {
            cita.setEstado(EstadoCita.CONFIRMADA);
            return citaRepository.save(cita);
        }).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    @Override
    public Cita cancelar(Long id) {
        return citaRepository.findById(id).map(cita -> {
            cita.setEstado(EstadoCita.CANCELADA);
            return citaRepository.save(cita);
        }).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }
}