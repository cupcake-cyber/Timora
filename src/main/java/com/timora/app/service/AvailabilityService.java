package com.timora.app.service;



import com.timora.app.dto.AvailabilityCreateDTO;
import com.timora.app.dto.AvailabilityDTO;
import com.timora.app.model.Availability;

import java.util.List;
import java.util.Optional;

public interface AvailabilityService {

    List<AvailabilityDTO> getMyAvailabilities();

    List<AvailabilityDTO> getAvailabilityBySupplier(Long supplierId);

    AvailabilityDTO createAvailability(AvailabilityCreateDTO dto);

    void updateStatus(Long id, String status);

    void delete(Long id);
}