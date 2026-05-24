package com.timora.app.service;

import com.timora.app.models.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {

    List<Appointment> findAll();

    Optional<Appointment> findById(Long id);

    Appointment save(Appointment appointment);

    Appointment update(Long id, Appointment appointment);

    void delete(Long id);

    // Cambios de estado
    Appointment confirm(Long id);

    Appointment cancel(Long id);


    List<Appointment> findByCustomerId(Long customerId);

    List<Appointment> findByServiceId(Long serviceId);

    List<Appointment> findByCompanyId(Long companyId);
}
