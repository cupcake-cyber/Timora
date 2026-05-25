package com.timora.app.service;

import com.timora.app.model.Availability;

import java.util.List;
import java.util.Optional;

public interface AvailabilityService {
    List<Availability> findAll();

    Optional<Availability> findById(Long id);

    Availability save(Availability availability);

    Availability update(Long id, Availability availability);

    void delete(Long id);
}
