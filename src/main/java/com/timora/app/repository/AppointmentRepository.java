package com.timora.app.repository;

import com.timora.app.models.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Por estado
    List<Appointment> findByStatus(AppointmentStatus status);

    // Por cliente
    List<Appointment> findByCustomerId(Long customerId);

    // Por usuario creador
    List<Appointment> findByCreatedByUserId(Long userId);

    // Por rango de fechas (start_time)
    List<Appointment> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    // Por proveedor (a través del service)
    List<Appointment> findByServiceSupplierId(Long supplierId);
}