package com.timora.app.service;

import com.timora.app.dto.availability.AvailabilityCreateDTO;
import com.timora.app.dto.availability.AvailabilityDTO;
import com.timora.app.dto.availability.AvailabilityPatchDTO;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityService {

    // CRUD
    AvailabilityDTO create(AvailabilityCreateDTO dto);
    AvailabilityDTO patch(Long id, AvailabilityPatchDTO dto);
    void delete(Long id);

    // Getters
    AvailabilityDTO getById(Long id);
    List<AvailabilityDTO> getAllByCompany();
    List<AvailabilityDTO> getAllBySupplier(Long supplierId);
    List<AvailabilityDTO> getBySupplierAndDate(Long supplierId, LocalDate date);

    // 🔴 Validación de overlapping - ya no se usa, pero mantenemos la firma
    void validateOverlap(Long supplierId, LocalDate startDate, LocalDate endDate, Long excludeId);
}