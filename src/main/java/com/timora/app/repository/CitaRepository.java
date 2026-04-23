package com.timora.app.repository;

import com.timora.app.models.Cita;
import com.timora.app.models.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByEstado(EstadoCita estado);

    List<Cita> findByClienteIdCliente(Long idCliente);

    List<Cita> findByClienteUsuarioIdUsuario(Long idUsuario);

    List<Cita> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Cita> findByServicioProveedorIdProveedor(Long idProveedor);
}