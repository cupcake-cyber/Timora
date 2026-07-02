package com.timora.app.service.impl;

import com.timora.app.dto.availability.AvailabilityCreateDTO;
import com.timora.app.dto.availability.AvailabilityDTO;
import com.timora.app.dto.availability.AvailabilityPatchDTO;
import com.timora.app.dto.security.CurrentUser;
import com.timora.app.exception.BusinessException;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.exception.NotFoundException;
import com.timora.app.model.Availability;
import com.timora.app.model.Company;
import com.timora.app.model.Supplier;
import com.timora.app.model.enums.AvailabilityStatus;
import com.timora.app.repository.AvailabilityRepository;
import com.timora.app.repository.CompanyRepository;
import com.timora.app.repository.SupplierRepository;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.AvailabilityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final SupplierRepository supplierRepository;
    private final CompanyRepository companyRepository;
    private final SecurityHelper securityHelper;
    private final AccessControlService auth;

    @Override
    @Transactional
    public AvailabilityDTO create(AvailabilityCreateDTO request) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        // Validar que el companyId existe
        if (request.getCompanyId() == null) {
            throw new BusinessException("Company ID is required");
        }

        // Validar que el supplierId existe
        if (request.getSupplierId() == null) {
            throw new BusinessException("Supplier ID is required");
        }

        // Validar que el supplier existe
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new NotFoundException("Supplier not found"));

        // Validar que el supplier pertenece a la compañía indicada
        if (!supplier.getCompany().getId().equals(request.getCompanyId())) {
            throw new BusinessException("Supplier does not belong to the specified company");
        }

        // Validar que la compañía existe
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Company not found"));

        // Control de acceso mejorado
        if (!auth.isOwner(currentUser)) {
            // Admin y User solo pueden operar en su compañía
            if (!currentUser.getCompanyId().equals(request.getCompanyId())) {
                throw new ForbiddenException("You are not allowed to perform this action in another company");
            }

            // User no puede crear disponibilidades
            if (!auth.isAdmin(currentUser)) {
                throw new ForbiddenException("You are not allowed to create availability");
            }
        }

        // Validar fechas
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new BusinessException("Start date and end date are required");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BusinessException("Start date must be before end date");
        }

        if (request.getStartDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Start date cannot be in the past");
        }

        // Validar tiempo
        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new BusinessException("Start time and end time are required");
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BusinessException("Start time must be before end time");
        }

        // Validar slot duration y capacity
        if (request.getSlotDurationMinutes() == null || request.getSlotDurationMinutes() <= 0) {
            throw new BusinessException("Slot duration must be greater than 0");
        }

        if (request.getCapacity() == null || request.getCapacity() <= 0) {
            throw new BusinessException("Capacity must be greater than 0");
        }

        // Validar overlapping
        validateOverlap(request.getSupplierId(), request.getStartDate(), request.getEndDate(), null);

        // Crear la entidad
        Availability availability = new Availability();
        availability.setCompany(company);
        availability.setSupplier(supplier);
        availability.setStartDate(request.getStartDate());
        availability.setEndDate(request.getEndDate());
        availability.setDayOfWeek(request.getDayOfWeek());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        availability.setRecurrenceType(request.getRecurrenceType());
        availability.setSlotDurationMinutes(request.getSlotDurationMinutes());
        availability.setCapacity(request.getCapacity());
        availability.setNotes(request.getNotes());
        availability.setStatus(AvailabilityStatus.ACTIVE);

        Availability saved = availabilityRepository.save(availability);

        return toDTO(saved);
    }

    @Override
    @Transactional
    public AvailabilityDTO patch(Long id, AvailabilityPatchDTO request) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Availability not found"));

        // Control de acceso mejorado
        if (!auth.isOwner(currentUser)) {
            // Admin y User solo pueden operar en su compañía
            if (!currentUser.getCompanyId().equals(availability.getCompany().getId())) {
                throw new ForbiddenException("You are not allowed to perform this action in another company");
            }

            // User no puede actualizar disponibilidades
            if (!auth.isAdmin(currentUser)) {
                throw new ForbiddenException("You are not allowed to update availability");
            }
        }

        // Validar fechas si vienen en el request
        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getStartDate().isAfter(request.getEndDate())) {
                throw new BusinessException("Start date must be before end date");
            }

            if (request.getStartDate().isBefore(LocalDate.now())) {
                throw new BusinessException("Start date cannot be in the past");
            }

            // Validar overlapping con los nuevos datos
            LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : availability.getStartDate();
            LocalDate endDate = request.getEndDate() != null ? request.getEndDate() : availability.getEndDate();

            validateOverlap(availability.getSupplier().getId(), startDate, endDate, id);
        }

        // Validar tiempos si vienen en el request
        if (request.getStartTime() != null && request.getEndTime() != null) {
            if (request.getStartTime().isAfter(request.getEndTime())) {
                throw new BusinessException("Start time must be before end time");
            }
        }

        // Actualizar campos
        if (request.getStartDate() != null) {
            availability.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            availability.setEndDate(request.getEndDate());
        }

        if (request.getDayOfWeek() != null) {
            availability.setDayOfWeek(request.getDayOfWeek());
        }

        if (request.getStartTime() != null) {
            availability.setStartTime(request.getStartTime());
        }

        if (request.getEndTime() != null) {
            availability.setEndTime(request.getEndTime());
        }

        if (request.getRecurrenceType() != null) {
            availability.setRecurrenceType(request.getRecurrenceType());
        }

        if (request.getSlotDurationMinutes() != null) {
            if (request.getSlotDurationMinutes() <= 0) {
                throw new BusinessException("Slot duration must be greater than 0");
            }
            availability.setSlotDurationMinutes(request.getSlotDurationMinutes());
        }

        if (request.getCapacity() != null) {
            if (request.getCapacity() <= 0) {
                throw new BusinessException("Capacity must be greater than 0");
            }
            availability.setCapacity(request.getCapacity());
        }

        if (request.getNotes() != null) {
            availability.setNotes(request.getNotes());
        }

        // Actualizar status si viene en el request
        if (request.getStatus() != null) {
            availability.setStatus(request.getStatus());
        }

        Availability saved = availabilityRepository.save(availability);

        return toDTO(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Availability not found"));

        // Control de acceso mejorado
        if (!auth.isOwner(currentUser)) {
            // Admin y User solo pueden operar en su compañía
            if (!currentUser.getCompanyId().equals(availability.getCompany().getId())) {
                throw new ForbiddenException("You are not allowed to perform this action in another company");
            }

            // User no puede eliminar disponibilidades
            if (!auth.isAdmin(currentUser)) {
                throw new ForbiddenException("You are not allowed to delete availability");
            }
        }

        // Soft delete - cambiar estado a INACTIVE
        availability.setStatus(AvailabilityStatus.INACTIVE);
        availabilityRepository.save(availability);
    }

    @Override
    public List<AvailabilityDTO> getAllByCompany() {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        List<Availability> availabilities;

        if (auth.isOwner(currentUser)) {
            // Owner ve todas las disponibilidades de todas las compañías (incluyendo INACTIVE)
            availabilities = availabilityRepository.findAll();
        } else {
            // Admin y User solo ven ACTIVE de su compañía
            availabilities = availabilityRepository.findByCompanyIdAndStatus(
                    currentUser.getCompanyId(),
                    AvailabilityStatus.ACTIVE
            );
        }

        return availabilities.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<AvailabilityDTO> getAllBySupplier(Long supplierId) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found"));

        // Control de acceso mejorado
        if (!auth.isOwner(currentUser)) {
            // Admin y User solo pueden ver de su compañía
            if (!currentUser.getCompanyId().equals(supplier.getCompany().getId())) {
                throw new ForbiddenException("You are not allowed to view availability from another company");
            }
        }

        List<Availability> availabilities;

        if (auth.isOwner(currentUser)) {
            // Owner ve todas las disponibilidades del supplier (incluyendo INACTIVE)
            availabilities = availabilityRepository.findBySupplierId(supplierId);
        } else {
            // Admin y User solo ven ACTIVE
            availabilities = availabilityRepository.findBySupplierIdAndStatus(
                    supplierId,
                    AvailabilityStatus.ACTIVE
            );
        }

        return availabilities.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<AvailabilityDTO> getBySupplierAndDate(Long supplierId, LocalDate date) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found"));

        // Control de acceso mejorado
        if (!auth.isOwner(currentUser)) {
            // Admin y User solo pueden ver de su compañía
            if (!currentUser.getCompanyId().equals(supplier.getCompany().getId())) {
                throw new ForbiddenException("You are not allowed to view availability from another company");
            }
        }

        // Siempre filtramos solo ACTIVE para este endpoint (incluso para owner)
        // Ya que es un endpoint de consulta específica para fechas
        List<Availability> availabilities = availabilityRepository.findBySupplierIdAndDate(
                supplierId,
                date,
                AvailabilityStatus.ACTIVE
        );

        return availabilities.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public AvailabilityDTO getById(Long id) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Availability not found"));

        // Control de acceso mejorado
        if (!auth.isOwner(currentUser)) {
            // Admin y User solo pueden ver de su compañía
            if (!currentUser.getCompanyId().equals(availability.getCompany().getId())) {
                throw new ForbiddenException("You are not allowed to view this availability");
            }
        }

        // Los usuarios normales solo pueden ver ACTIVE
        if (!auth.isOwner(currentUser) && availability.getStatus() == AvailabilityStatus.INACTIVE) {
            throw new NotFoundException("Availability not found");
        }

        return toDTO(availability);
    }

    @Override
    public void validateOverlap(Long supplierId, LocalDate startDate, LocalDate endDate, Long excludeId) {

        boolean hasOverlap;

        if (excludeId != null) {
            hasOverlap = availabilityRepository.existsOverlappingExcludingId(
                    supplierId,
                    startDate,
                    endDate,
                    excludeId,
                    AvailabilityStatus.ACTIVE
            );
        } else {
            hasOverlap = availabilityRepository.existsOverlapping(
                    supplierId,
                    startDate,
                    endDate,
                    AvailabilityStatus.ACTIVE
            );
        }

        if (hasOverlap) {
            throw new BusinessException("Availability overlaps with existing availability for this supplier");
        }
    }

    private AvailabilityDTO toDTO(Availability availability) {

        AvailabilityDTO dto = new AvailabilityDTO();

        dto.setCompanyId(availability.getCompany().getId());
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
}