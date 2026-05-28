package com.timora.app.service.impl;


import com.timora.app.dto.AvailabilityCreateDTO;
import com.timora.app.dto.AvailabilityDTO;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.model.Availability;
import com.timora.app.model.Supplier;
import com.timora.app.model.User;
import com.timora.app.model.enums.AvailabilityRecurring;
import com.timora.app.model.enums.AvailabilityStatus;
import com.timora.app.model.enums.GlobalRole;
import com.timora.app.repository.AvailabilityRepository;
import com.timora.app.repository.SupplierRepository;
import com.timora.app.repository.UserRepository;
import com.timora.app.service.AvailabilityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    // =========================
    // GET CURRENT USER
    // =========================
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByLoginEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    // =========================
    // GET SUPPLIER FROM USER
    // =========================
    private Supplier getCurrentSupplier(User user) {

        return supplierRepository
                .findByUserIdAndCompanyId(
                        user.getId(),
                        user.getCompany().getId()
                )
                .orElseThrow(() ->
                        new ForbiddenException("User is not a supplier")
                );
    }

    // =========================
    // CHECK OWNERSHIP
    // =========================
    private void checkOwnership(User user, Availability availability) {

        Supplier supplier = getCurrentSupplier(user);

        if (!availability.getSupplier().getId().equals(supplier.getId())) {
            throw new ForbiddenException("Not your availability");
        }
    }

    // =========================
    // READ (ONLY MINE)
    // =========================
    @Override
    public List<AvailabilityDTO> getMyAvailabilities() {

        User user = getCurrentUser();
        Supplier supplier = getCurrentSupplier(user);

        return availabilityRepository
                .findByCompanyIdAndSupplierId(
                        user.getCompany().getId(),
                        supplier.getId()
                )
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // =========================
    // CREATE
    // =========================
    @Override
    public AvailabilityDTO createAvailability(AvailabilityCreateDTO dto) {

        User user = getCurrentUser();
        Supplier supplier = getCurrentSupplier(user);

        validateBusinessRules(dto, user.getCompany().getId());

        Availability availability = new Availability();

        availability.setCompany(user.getCompany());
        availability.setSupplier(supplier);

        availability.setStartDate(dto.getStartDate());
        availability.setEndDate(dto.getEndDate());
        availability.setDayOfWeek(dto.getDayOfWeek());
        availability.setStartTime(dto.getStartTime());
        availability.setEndTime(dto.getEndTime());
        availability.setRecurrenceType(dto.getRecurrenceType());
        availability.setSlotDurationMinutes(dto.getSlotDurationMinutes());
        availability.setCapacity(dto.getCapacity());
        availability.setNotes(dto.getNotes());
        availability.setStatus(AvailabilityStatus.ACTIVE);

        return mapToDTO(availabilityRepository.save(availability));
    }

    // =========================
    // UPDATE STATUS (PATCH)
    // =========================
    @Override
    public void updateStatus(Long id, String status) {

        User user = getCurrentUser();

        Availability availability = availabilityRepository
                .findByIdAndCompanyId(id, user.getCompany().getId())
                .orElseThrow(() -> new EntityNotFoundException("Availability not found"));

        checkOwnership(user, availability);

        availability.setStatus(
                AvailabilityStatus.valueOf(status.toUpperCase())
        );

        availabilityRepository.save(availability);
    }

    // =========================
    // DELETE (SOFT DELETE)
    // =========================
    @Override
    public void delete(Long id) {

        User user = getCurrentUser();

        Availability availability = availabilityRepository
                .findByIdAndCompanyId(id, user.getCompany().getId())
                .orElseThrow(() -> new EntityNotFoundException("Availability not found"));

        checkOwnership(user, availability);

        availability.setStatus(AvailabilityStatus.INACTIVE);

        availabilityRepository.save(availability);
    }

    // =========================
    // MAPPER
    // =========================
    private AvailabilityDTO mapToDTO(Availability availability) {

        AvailabilityDTO dto = new AvailabilityDTO();

        dto.setId(availability.getId());
        dto.setSupplierId(availability.getSupplier().getId());
        dto.setStartDate(availability.getStartDate());
        dto.setEndDate(availability.getEndDate());
        dto.setDayOfWeek(availability.getDayOfWeek());
        dto.setStartTime(availability.getStartTime());
        dto.setEndTime(availability.getEndTime());
        dto.setRecurrenceType(availability.getRecurrenceType());
        dto.setSlotDurationMinutes(availability.getSlotDurationMinutes());
        dto.setCapacity(availability.getCapacity());
        dto.setStatus(availability.getStatus());
        dto.setNotes(availability.getNotes());

        return dto;
    }

    // =========================
    // BUSINESS RULES
    // =========================
    private void validateBusinessRules(AvailabilityCreateDTO dto, Long companyId) {

        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("Invalid date range");
        }

        List<Availability> overlaps =
                availabilityRepository.findOverlappingAvailabilities(
                        companyId,
                        null, // no supplier filter here (you can refine later)
                        dto.getStartDate(),
                        dto.getEndDate(),
                        dto.getDayOfWeek(),
                        dto.getStartTime(),
                        dto.getEndTime()
                );

        if (!overlaps.isEmpty()) {
            throw new IllegalArgumentException("Availability overlap detected");
        }
    }
    @Override
    public List<AvailabilityDTO> getAvailabilityBySupplier(Long supplierId) {

        User user = getCurrentUser();

        Supplier supplier = supplierRepository
                .findByIdAndCompanyId(supplierId, user.getCompany().getId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        return availabilityRepository
                .findByCompanyIdAndSupplierId(
                        user.getCompany().getId(),
                        supplier.getId()
                )
                .stream()
                .map(this::mapToDTO)
                .toList();
    }
}