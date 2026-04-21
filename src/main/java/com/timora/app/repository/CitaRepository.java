package com.timora.app.repository;

import com.timora.app.models.Cita;
import com.timora.app.models.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita,Integer> {

    List<Cita> findByEstado(EstadoCita estadoCita);

    List<Cita> findByClienteId(Integer clienteId);

    List<Cita> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
}
