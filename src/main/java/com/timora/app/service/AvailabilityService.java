package com.timora.app.service;



import com.timora.app.dto.AvailabilityCreateDTO;
import com.timora.app.dto.AvailabilityDTO;
import com.timora.app.model.Availability;

import java.util.List;
import java.util.Optional;

public interface AvailabilityService {
    Availability create(Availability availability);
    List<AvailabilityDTO> getAll();
    List<AvailabilityDTO> getBySupplierId(Long supplierId);
    AvailabilityDTO getById(Long id);
    AvailabilityDTO createAvailability(AvailabilityCreateDTO dto);
    Availability patch(Long id, Availability availability);
    void delete(Long id);
}