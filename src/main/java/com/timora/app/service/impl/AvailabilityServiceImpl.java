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
import com.timora.app.model.Person;
import com.timora.app.model.Supplier;
import com.timora.app.model.enums.AvailabilityStatus;
import com.timora.app.model.enums.Permission;
import com.timora.app.repository.AvailabilityRepository;
import com.timora.app.security.AccessControlBaseService;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.AvailabilityService;
import com.timora.app.service.CompanyService;
import com.timora.app.service.PersonService;
import com.timora.app.service.SupplierService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final CompanyService companyService;
    private final SupplierService supplierService;
    private final PersonService personService;
    private final SecurityHelper securityHelper;
    private final AccessControlService access;
    private final AccessControlBaseService accessBase;

    // =========================
    // CREATE
    // =========================

    @Override
    @Transactional
    public AvailabilityDTO create(AvailabilityCreateDTO request) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        // =========================
        // 1. VALIDACIONES BÁSICAS
        // =========================

        if (request.getCompanyId() == null) {
            throw new BusinessException("Company ID is required");
        }

        if (request.getSupplierId() == null) {
            throw new BusinessException("Supplier ID is required");
        }

        Supplier supplier = supplierService.findById(request.getSupplierId());

        if (!supplier.getCompany().getId().equals(request.getCompanyId())) {
            throw new BusinessException("Supplier does not belong to the specified company");
        }

        Company company = companyService.getByIdEntity(request.getCompanyId());

        // =========================
        // 2. VALIDACIONES DE FECHAS Y HORARIOS
        // =========================

        if (request.getStartDate() == null) {
            throw new BusinessException("Start date is required");
        }

        if (request.getEndDate() != null &&
                request.getStartDate().isAfter(request.getEndDate())) {
            throw new BusinessException("Start date must be before end date");
        }

        if (request.getStartDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Start date cannot be in the past");
        }

        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new BusinessException("Start time and end time are required");
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BusinessException("Start time must be before end time");
        }

        if (request.getSlotDurationMinutes() == null || request.getSlotDurationMinutes() <= 0) {
            throw new BusinessException("Slot duration must be greater than 0");
        }

        if (request.getCapacity() == null || request.getCapacity() <= 0) {
            throw new BusinessException("Capacity must be greater than 0");
        }

        // =========================
        // 3. VALIDACIÓN DE DÍAS SEGÚN RECURRENCIA
        // =========================

        if (request.getRecurrenceType() != null) {
            switch (request.getRecurrenceType()) {
                case WEEKLY:
                    if (!hasAnyDaySelected(request)) {
                        throw new BusinessException(
                                "At least one day must be selected for WEEKLY recurrence"
                        );
                    }
                    break;
                case NONE:
                    if (hasAnyDaySelected(request)) {
                        throw new BusinessException(
                                "Days should not be selected for NONE recurrence"
                        );
                    }
                    break;
                case DAILY:
                case MONTHLY:
                case YEARLY:
                case CUSTOM:
                    // Estos tipos ignoran los días booleanos
                    break;
            }
        }

        // =========================
        // 4. 🔐 CONTROL DE ACCESO
        // =========================

        checkCreatePermission(currentUser, supplier, request.getCompanyId());

        // =========================
        // 5. CREAR ENTIDAD (SIEMPRE ACTIVE)
        // =========================

        Availability availability = new Availability();
        availability.setCompany(company);
        availability.setSupplier(supplier);
        availability.setStartDate(request.getStartDate());
        availability.setEndDate(request.getEndDate());

        // Días de la semana
        availability.setMonday(request.getMonday() != null && request.getMonday());
        availability.setTuesday(request.getTuesday() != null && request.getTuesday());
        availability.setWednesday(request.getWednesday() != null && request.getWednesday());
        availability.setThursday(request.getThursday() != null && request.getThursday());
        availability.setFriday(request.getFriday() != null && request.getFriday());
        availability.setSaturday(request.getSaturday() != null && request.getSaturday());
        availability.setSunday(request.getSunday() != null && request.getSunday());

        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        availability.setRecurrenceType(request.getRecurrenceType());
        availability.setSlotDurationMinutes(request.getSlotDurationMinutes());
        availability.setCapacity(request.getCapacity());
        availability.setNotes(request.getNotes());
        availability.setStatus(AvailabilityStatus.ACTIVE); // 🔴 SIEMPRE ACTIVE al crear

        Availability saved = availabilityRepository.save(availability);

        return toDTO(saved);
    }

    // =========================
    // PATCH (UPDATE) - Permite cambiar status
    // =========================

    @Override
    @Transactional
    public AvailabilityDTO patch(Long id, AvailabilityPatchDTO request) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Availability not found"));

        // =========================
        // 🔐 CONTROL DE ACCESO
        // =========================

        checkUpdatePermission(currentUser, availability);

        // =========================
        // VALIDACIONES
        // =========================

        // Validar fechas si vienen en el request
        if (request.getStartDate() != null || request.getEndDate() != null) {
            LocalDate startDate = request.getStartDate() != null
                    ? request.getStartDate()
                    : availability.getStartDate();
            LocalDate endDate = request.getEndDate() != null
                    ? request.getEndDate()
                    : availability.getEndDate();

            if (startDate.isAfter(endDate)) {
                throw new BusinessException("Start date must be before end date");
            }

            if (startDate.isBefore(LocalDate.now())) {
                throw new BusinessException("Start date cannot be in the past");
            }
        }

        // Validar tiempos si vienen en el request
        if (request.getStartTime() != null && request.getEndTime() != null) {
            if (request.getStartTime().isAfter(request.getEndTime())) {
                throw new BusinessException("Start time must be before end time");
            }
        }

        // Validar slot duration y capacity
        if (request.getSlotDurationMinutes() != null && request.getSlotDurationMinutes() <= 0) {
            throw new BusinessException("Slot duration must be greater than 0");
        }

        if (request.getCapacity() != null && request.getCapacity() <= 0) {
            throw new BusinessException("Capacity must be greater than 0");
        }

        // =========================
        // ACTUALIZAR CAMPOS
        // =========================

        if (request.getStartDate() != null) {
            availability.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            availability.setEndDate(request.getEndDate());
        }

        if (request.getStartTime() != null) {
            availability.setStartTime(request.getStartTime());
        }

        if (request.getEndTime() != null) {
            availability.setEndTime(request.getEndTime());
        }

        // Días de la semana
        if (request.getMonday() != null) {
            availability.setMonday(request.getMonday());
        }
        if (request.getTuesday() != null) {
            availability.setTuesday(request.getTuesday());
        }
        if (request.getWednesday() != null) {
            availability.setWednesday(request.getWednesday());
        }
        if (request.getThursday() != null) {
            availability.setThursday(request.getThursday());
        }
        if (request.getFriday() != null) {
            availability.setFriday(request.getFriday());
        }
        if (request.getSaturday() != null) {
            availability.setSaturday(request.getSaturday());
        }
        if (request.getSunday() != null) {
            availability.setSunday(request.getSunday());
        }

        if (request.getRecurrenceType() != null) {
            availability.setRecurrenceType(request.getRecurrenceType());
        }

        if (request.getSlotDurationMinutes() != null) {
            availability.setSlotDurationMinutes(request.getSlotDurationMinutes());
        }

        if (request.getCapacity() != null) {
            availability.setCapacity(request.getCapacity());
        }

        if (request.getNotes() != null) {
            availability.setNotes(request.getNotes());
        }

        // 🔴 PERMITIR CAMBIAR EL STATUS (ACTIVE/INACTIVE)
        if (request.getStatus() != null) {
            availability.setStatus(request.getStatus());
        }

        Availability saved = availabilityRepository.save(availability);

        return toDTO(saved);
    }

    // =========================
    // DELETE - Hard Delete (Eliminación física)
    // =========================

    @Override
    @Transactional
    public void delete(Long id) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Availability not found"));

        // =========================
        // 🔐 CONTROL DE ACCESO
        // =========================

        checkDeletePermission(currentUser, availability);

        // 🔴 HARD DELETE - Eliminación física de la base de datos
        availabilityRepository.delete(availability);
    }

    // =========================
    // GET BY ID
    // =========================

    @Override
    public AvailabilityDTO getById(Long id) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Availability not found"));

        checkReadPermission(currentUser, availability);

        return toDTO(availability);
    }

    // =========================
    // GET ALL BY COMPANY
    // =========================

    @Override
    public List<AvailabilityDTO> getAllByCompany() {

        CurrentUser currentUser = securityHelper.getCurrentUser();
        List<Availability> availabilities;

        if (accessBase.isOwner(currentUser)) {
            availabilities = availabilityRepository.findAll();
        } else if (accessBase.isAdmin(currentUser)) {
            availabilities = availabilityRepository.findByCompanyId(currentUser.getCompanyId());
            availabilities = availabilities.stream()
                    .filter(a -> hasReadAccess(currentUser, a))
                    .toList();
        } else {
            // USER: Debe poder ver disponibilidades donde:
            // 1. Es supplier (sus propias disponibilidades)
            // 2. Tiene permisos en user_supplier_permissions

            Set<Long> supplierIds = new HashSet<>();

            // 🔥 CASO 1: Si el usuario es supplier, agregar su propio supplierId
            try {
                Person currentPerson = personService.findById(currentUser.getPersonId());
                Supplier currentSupplier = currentPerson.getSupplier();
                if (currentSupplier != null) {
                    supplierIds.add(currentSupplier.getId());
                }
            } catch (Exception e) {
                // Si falla, continuar
            }

            // 🔥 CASO 2: Suppliers donde tiene permisos (user_supplier_permissions)
            List<Supplier> accessibleSuppliers = supplierService.findByUserId(currentUser.getUserId());
            if (!accessibleSuppliers.isEmpty()) {
                supplierIds.addAll(
                        accessibleSuppliers.stream()
                                .map(Supplier::getId)
                                .collect(Collectors.toSet())
                );
            }

            if (supplierIds.isEmpty()) {
                return List.of();
            }

            availabilities = availabilityRepository.findBySupplierIds(new ArrayList<>(supplierIds));

            // Filtrar por permisos de lectura
            availabilities = availabilities.stream()
                    .filter(a -> hasReadAccess(currentUser, a))
                    .toList();
        }

        return availabilities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    // =========================
    // GET ALL BY SUPPLIER
    // =========================

    @Override
    public List<AvailabilityDTO> getAllBySupplier(Long supplierId) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Supplier supplier = supplierService.findById(supplierId);

        // Verificar acceso al supplier
        access.requireSupplierAccess(currentUser, supplier);

        List<Availability> availabilities = availabilityRepository.findBySupplierId(supplierId);

        availabilities = availabilities.stream()
                .filter(a -> hasReadAccess(currentUser, a))
                .toList();

        return availabilities.stream()
                .map(this::toDTO)
                .toList();
    }
    // =========================
    // GET BY SUPPLIER AND DATE
    // =========================

    @Override
    public List<AvailabilityDTO> getBySupplierAndDate(Long supplierId, LocalDate date) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Supplier supplier = supplierService.findById(supplierId);

        access.requireSupplierAccess(currentUser, supplier);

        List<Availability> availabilities = availabilityRepository.findBySupplierId(supplierId);

        // 🔴 SOLO disponibilidades ACTIVE para el calendario
        availabilities = availabilities.stream()
                .filter(a -> a.getStatus() == AvailabilityStatus.ACTIVE)
                .filter(a -> isDateInRange(date, a))
                .filter(a -> hasReadAccess(currentUser, a))
                .toList();

        return availabilities.stream()
                .map(this::toDTO)
                .toList();
    }

    // =========================
    // VALIDACIÓN DE OVERLAPPING - ELIMINADA
    // =========================

    @Override
    public void validateOverlap(Long supplierId, LocalDate startDate, LocalDate endDate, Long excludeId) {
        // 🔴 NO HACE NADA - Overlapping no se valida
        // Puedes dejarlo vacío o lanzar un mensaje de que no se usa
    }

    // =========================
    // MÉTODOS DE PERMISOS
    // =========================

    private void checkCreatePermission(CurrentUser currentUser, Supplier supplier, Long companyId) {
        if (accessBase.isOwner(currentUser)) {
            return;
        }

        if (accessBase.isAdmin(currentUser)) {
            if (!currentUser.getCompanyId().equals(companyId)) {
                throw new ForbiddenException(
                        "You are not allowed to create availability in another company"
                );
            }
            return;
        }

        if (!currentUser.getCompanyId().equals(companyId)) {
            throw new ForbiddenException(
                    "You are not allowed to create availability in another company"
            );
        }

        Person currentPerson = personService.findById(currentUser.getPersonId());
        Supplier currentSupplier = currentPerson.getSupplier();

        if (currentSupplier != null &&
                currentSupplier.getId().equals(supplier.getId())) {
            return;
        }

        access.requirePermission(currentUser, supplier, Permission.AVAILABILITY_CREATE);
    }

    private void checkReadPermission(CurrentUser currentUser, Availability availability) {
        if (accessBase.isOwner(currentUser)) {
            return;
        }

        if (accessBase.isAdmin(currentUser)) {
            if (!currentUser.getCompanyId().equals(availability.getCompany().getId())) {
                throw new ForbiddenException(
                        "You are not allowed to view availability from another company"
                );
            }
            return;
        }

        if (!currentUser.getCompanyId().equals(availability.getCompany().getId())) {
            throw new ForbiddenException(
                    "You are not allowed to view availability from another company"
            );
        }

        Person currentPerson = personService.findById(currentUser.getPersonId());
        Supplier currentSupplier = currentPerson.getSupplier();

        if (currentSupplier != null &&
                currentSupplier.getId().equals(availability.getSupplier().getId())) {
            return;
        }

        access.requirePermission(
                currentUser,
                availability.getSupplier(),
                Permission.AVAILABILITY_READ
        );
    }

    private void checkUpdatePermission(CurrentUser currentUser, Availability availability) {
        if (accessBase.isOwner(currentUser)) {
            return;
        }

        if (accessBase.isAdmin(currentUser)) {
            if (!currentUser.getCompanyId().equals(availability.getCompany().getId())) {
                throw new ForbiddenException(
                        "You are not allowed to update availability from another company"
                );
            }
            return;
        }

        if (!currentUser.getCompanyId().equals(availability.getCompany().getId())) {
            throw new ForbiddenException(
                    "You are not allowed to update availability from another company"
            );
        }

        Person currentPerson = personService.findById(currentUser.getPersonId());
        Supplier currentSupplier = currentPerson.getSupplier();

        if (currentSupplier != null &&
                currentSupplier.getId().equals(availability.getSupplier().getId())) {
            return;
        }

        access.requirePermission(
                currentUser,
                availability.getSupplier(),
                Permission.AVAILABILITY_UPDATE
        );
    }

    private void checkDeletePermission(CurrentUser currentUser, Availability availability) {
        if (accessBase.isOwner(currentUser)) {
            return;
        }

        if (accessBase.isAdmin(currentUser)) {
            if (!currentUser.getCompanyId().equals(availability.getCompany().getId())) {
                throw new ForbiddenException(
                        "You are not allowed to delete availability from another company"
                );
            }
            return;
        }

        if (!currentUser.getCompanyId().equals(availability.getCompany().getId())) {
            throw new ForbiddenException(
                    "You are not allowed to delete availability from another company"
            );
        }

        Person currentPerson = personService.findById(currentUser.getPersonId());
        Supplier currentSupplier = currentPerson.getSupplier();

        if (currentSupplier != null &&
                currentSupplier.getId().equals(availability.getSupplier().getId())) {
            return;
        }

        access.requirePermission(
                currentUser,
                availability.getSupplier(),
                Permission.AVAILABILITY_DELETE
        );
    }

    private boolean hasReadAccess(CurrentUser currentUser, Availability availability) {
        try {
            checkReadPermission(currentUser, availability);
            return true;
        } catch (ForbiddenException e) {
            return false;
        }
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================

    private boolean hasAnyDaySelected(AvailabilityCreateDTO request) {
        return Boolean.TRUE.equals(request.getMonday()) ||
                Boolean.TRUE.equals(request.getTuesday()) ||
                Boolean.TRUE.equals(request.getWednesday()) ||
                Boolean.TRUE.equals(request.getThursday()) ||
                Boolean.TRUE.equals(request.getFriday()) ||
                Boolean.TRUE.equals(request.getSaturday()) ||
                Boolean.TRUE.equals(request.getSunday());
    }

    private boolean isDateInRange(LocalDate date, Availability availability) {
        LocalDate start = availability.getStartDate();
        LocalDate end = availability.getEndDate();

        if (date.isBefore(start)) {
            return false;
        }

        if (end != null && date.isAfter(end)) {
            return false;
        }

        if (availability.getRecurrenceType() != null) {
            switch (availability.getRecurrenceType()) {
                case WEEKLY:
                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    return switch (dayOfWeek) {
                        case MONDAY -> availability.getMonday();
                        case TUESDAY -> availability.getTuesday();
                        case WEDNESDAY -> availability.getWednesday();
                        case THURSDAY -> availability.getThursday();
                        case FRIDAY -> availability.getFriday();
                        case SATURDAY -> availability.getSaturday();
                        case SUNDAY -> availability.getSunday();
                    };
                case MONTHLY:
                    return date.getDayOfMonth() == availability.getStartDate().getDayOfMonth();
                case YEARLY:
                    return date.getMonth() == availability.getStartDate().getMonth() &&
                            date.getDayOfMonth() == availability.getStartDate().getDayOfMonth();
                case DAILY:
                case NONE:
                case CUSTOM:
                    return true;
                default:
                    return true;
            }
        }

        return true;
    }

    // =========================
    // TO DTO
    // =========================

    private AvailabilityDTO toDTO(Availability availability) {
        AvailabilityDTO dto = new AvailabilityDTO();

        dto.setId(availability.getId());
        dto.setCompanyId(availability.getCompany().getId());
        dto.setSupplierId(availability.getSupplier().getId());
        dto.setStartDate(availability.getStartDate());
        dto.setEndDate(availability.getEndDate());
        dto.setStartTime(availability.getStartTime());
        dto.setEndTime(availability.getEndTime());

        dto.setMonday(availability.getMonday());
        dto.setTuesday(availability.getTuesday());
        dto.setWednesday(availability.getWednesday());
        dto.setThursday(availability.getThursday());
        dto.setFriday(availability.getFriday());
        dto.setSaturday(availability.getSaturday());
        dto.setSunday(availability.getSunday());

        dto.setRecurrenceType(availability.getRecurrenceType());
        dto.setSlotDurationMinutes(availability.getSlotDurationMinutes());
        dto.setCapacity(availability.getCapacity());
        dto.setStatus(availability.getStatus());
        dto.setNotes(availability.getNotes());
        dto.setCreatedAt(availability.getCreatedAt());

        return dto;
    }
}