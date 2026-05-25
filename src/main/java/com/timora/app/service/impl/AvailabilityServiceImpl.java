package com.timora.app.service.impl;

import com.timora.app.model.Availability;
import com.timora.app.repository.AvailabilityRepository;
import com.timora.app.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    @Override
    public List<Availability> findAll() {
        return availabilityRepository.findAll();
    }

    @Override
    public Optional<Availability> findById(Long id) {
        return availabilityRepository.findById(id);
    }

    @Override
    public Availability save(Availability availability) {
        return availabilityRepository.save(availability);
    }

    @Override
    public Availability update(Long id, Availability availability) {

        Availability existing = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        existing.setCompany(availability.getCompany());
        existing.setSupplier(availability.getSupplier());

        existing.setStartDate(availability.getStartDate());
        existing.setEndDate(availability.getEndDate());

        existing.setDayOfWeek(availability.getDayOfWeek());

        existing.setStartTime(availability.getStartTime());
        existing.setEndTime(availability.getEndTime());

        existing.setRecurrenceType(availability.getRecurrenceType());

        existing.setSlotDurationMinutes(
                availability.getSlotDurationMinutes()
        );

        existing.setCapacity(availability.getCapacity());

        existing.setStatus(availability.getStatus());

        existing.setNotes(availability.getNotes());

        return availabilityRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        availabilityRepository.deleteById(id);
    }
}

