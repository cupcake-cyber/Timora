package com.timora.app.service.impl;


import com.timora.app.dto.AvailabilityCreateDTO;
import com.timora.app.dto.AvailabilityDTO;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    public AvailabilityServiceImpl(
            AvailabilityRepository availabilityRepository,
            SupplierRepository supplierRepository,
            UserRepository userRepository
    ) {
        this.availabilityRepository = availabilityRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AvailabilityDTO> findAll() {

        User currentUser = getCurrentUser();
        Long companyId = currentUser.getCompany().getId();

        if (currentUser.getGlobalRole() == GlobalRole.OWNER || currentUser.getGlobalRole() == GlobalRole.COMPANY_ADMIN) {
            return availabilityRepository.findByCompanyId(companyId)
                    .stream()
                    .map(this::mapToDTO)
                    .toList();
        }

        Long supplierId = obtenerSupplierIdDesdeUser(currentUser);

        return availabilityRepository.findByCompanyIdAndSupplierId(companyId, supplierId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<AvailabilityDTO> getAvailabilityBySupplier(
            Long supplierId
    ) {

        Long companyId = getCurrentUser().getCompany().getId();

        validateSupplierOwnership(supplierId, companyId);

        return availabilityRepository
                .findByCompanyIdAndSupplierId(
                        companyId,
                        supplierId
                )
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public AvailabilityDTO createAvailability(
            AvailabilityCreateDTO dto
    ) {

        User user = getCurrentUser();

        Long companyId = user.getCompany().getId();

        Supplier supplier =
                validateSupplierOwnership(
                        dto.getSupplierId(),
                        companyId
                );

        validateBusinessRules(dto, companyId);

        Availability availability = new Availability();

        availability.setCompany(user.getCompany());
        availability.setSupplier(supplier);
        availability.setStartDate(dto.getStartDate());
        availability.setEndDate(dto.getEndDate());
        availability.setDayOfWeek(dto.getDayOfWeek());
        availability.setStartTime(dto.getStartTime());
        availability.setEndTime(dto.getEndTime());
        availability.setRecurrenceType(
                dto.getRecurrenceType()
        );
        availability.setSlotDurationMinutes(
                dto.getSlotDurationMinutes()
        );
        availability.setCapacity(dto.getCapacity());
        availability.setNotes(dto.getNotes());
        availability.setStatus(
                AvailabilityStatus.ACTIVE
        );

        availabilityRepository.save(availability);

        return mapToDTO(availability);
    }

    @Override
    public void updateStatus(Long id, String status) {

        Long companyId = getCurrentUser()
                .getCompany()
                .getId();

        Availability availability =
                availabilityRepository
                        .findByIdAndCompanyId(
                                id,
                                companyId
                        )
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Availability not found"
                                ));

        availability.setStatus(
                AvailabilityStatus.valueOf(
                        status.toUpperCase()
                )
        );

        availabilityRepository.save(availability);
    }

    @Override
    public void delete(Long id) {

        updateStatus(id, "INACTIVE");
    }

    private void validateBusinessRules(
            AvailabilityCreateDTO dto,
            Long companyId
    ) {

        if (!dto.getStartTime()
                .isBefore(dto.getEndTime())) {

            throw new IllegalArgumentException(
                    "Start time must be before end time"
            );
        }

        if (dto.getEndDate()
                .isBefore(dto.getStartDate())) {

            throw new IllegalArgumentException(
                    "Invalid date range"
            );
        }

        if (dto.getRecurrenceType()
                == AvailabilityRecurring.WEEKLY
                && dto.getDayOfWeek() == null) {

            throw new IllegalArgumentException(
                    "Weekly recurrence requires dayOfWeek"
            );
        }

        if ((dto.getRecurrenceType()
                == AvailabilityRecurring.NONE
                || dto.getRecurrenceType()
                == AvailabilityRecurring.DAILY)
                && dto.getDayOfWeek() != null) {

            throw new IllegalArgumentException(
                    "dayOfWeek not allowed"
            );
        }

        List<Availability> overlaps =
                availabilityRepository
                        .findOverlappingAvailabilities(
                                companyId,
                                dto.getSupplierId(),
                                dto.getStartDate(),
                                dto.getEndDate(),
                                dto.getDayOfWeek(),
                                dto.getStartTime(),
                                dto.getEndTime()
                        );

        if (!overlaps.isEmpty()) {

            throw new IllegalArgumentException(
                    "Availability overlap detected"
            );
        }
    }

    private Supplier validateSupplierOwnership(
            Long supplierId,
            Long companyId
    ) {

        return supplierRepository
                .findByIdAndCompanyId(
                        supplierId,
                        companyId
                )
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Supplier not found"
                        ));
    }

    private User getCurrentUser() {

        String email =
                org.springframework.security.core.context
                        .SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository.findByLoginEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User not found"
                        ));
    }

    private AvailabilityDTO mapToDTO(
            Availability availability
    ) {

        AvailabilityDTO dto =
                new AvailabilityDTO();

        dto.setId(availability.getId());
        dto.setSupplierId(
                availability.getSupplier().getId()
        );
        dto.setStartDate(
                availability.getStartDate()
        );
        dto.setEndDate(
                availability.getEndDate()
        );
        dto.setDayOfWeek(
                availability.getDayOfWeek()
        );
        dto.setStartTime(
                availability.getStartTime()
        );
        dto.setEndTime(
                availability.getEndTime()
        );
        dto.setRecurrenceType(
                availability.getRecurrenceType()
        );
        dto.setSlotDurationMinutes(
                availability.getSlotDurationMinutes()
        );
        dto.setCapacity(
                availability.getCapacity()
        );
        dto.setStatus(
                availability.getStatus()
        );
        dto.setNotes(
                availability.getNotes()
        );

        return dto;
    }

    private Long obtenerSupplierIdDesdeUser(User currentUser) {
        return supplierRepository.findByUserIdAndCompanyId(currentUser.getId(), currentUser.getCompany().getId())
                .orElseThrow(() -> new RuntimeException(
                        "El usuario actual no tiene un perfil de proveedor activo asignado en esta empresa."
                ))
                .getId();
    }
}

