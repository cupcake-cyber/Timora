package com.timora.app.service;

import com.timora.app.models.Availability;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {

    List<Availability> findAll();

    Optional<Availability> findById(Long id);

    Availability save(Availability appointment);

    Availability update(Long id, Availability appointment);

    void delete(Long id);

    // Cambios de estado
    Availability confirm(Long id);

    Availability cancel(Long id);


    List<Availability> findByCustomerId(Long customerId);

    List<Availability> findByServiceId(Long serviceId);

    List<Availability> findByCompanyId(Long companyId);
}
