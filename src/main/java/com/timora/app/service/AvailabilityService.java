package com.timora.app.service;

import com.timora.app.dto.availability.AvailabilityCreateDTO;
import com.timora.app.dto.availability.AvailabilityDTO;
import com.timora.app.dto.availability.AvailabilityPatchDTO;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityService {

    AvailabilityDTO create(AvailabilityCreateDTO request);

    AvailabilityDTO patch(Long id, AvailabilityPatchDTO request);

    void delete(Long id);

    List<AvailabilityDTO> getAllByCompany();

    List<AvailabilityDTO> getAllBySupplier(Long supplierId);

    List<AvailabilityDTO> getBySupplierAndDate(Long supplierId, LocalDate date);

    AvailabilityDTO getById(Long id);

    void validateOverlap(Long supplierId, LocalDate startDate, LocalDate endDate, Long excludeId);
}