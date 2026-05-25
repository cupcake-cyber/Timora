package com.timora.app.service;

import com.timora.app.models.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    List<Booking> findAll();

    Optional<Booking> findById(Long id);

    Booking save(Booking booking);

    Booking update(Long id, Booking booking);

    void delete(Long id);

    // Cambios de estado
    Booking confirm(Long id);

    Booking cancel(Long id);

    // Filtros
    List<Booking> findByCustomerId(Long customerId);

    List<Booking> findByServiceId(Long serviceId);

    List<Booking> findByCompanyId(Long companyId);
}