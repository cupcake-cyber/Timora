package com.timora.app.service.impl;

import com.timora.app.dto.booking.BookingCreateDTO;
import com.timora.app.dto.booking.BookingDTO;
import com.timora.app.dto.booking.BookingPatchDTO;
import com.timora.app.dto.security.CurrentUser;
import com.timora.app.exception.BusinessException;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.exception.NotFoundException;
import com.timora.app.model.*;
import com.timora.app.model.enums.BookingStatus;
import com.timora.app.model.enums.BookingType;
import com.timora.app.model.enums.Permission;
import com.timora.app.repository.BookingRepository;
import com.timora.app.security.AccessControlBaseService;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.security.AvailabilityValidatorService;
import com.timora.app.service.*;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CompanyService companyService;
    private final ServiceService serviceService;
    private final CustomerService customerService;
    private final UserService userService;
    private final SupplierService supplierService;
    private final PersonService personService;
    private final SecurityHelper securityHelper;
    private final AccessControlBaseService accessBase;
    private final AccessControlService access;
    private final AvailabilityValidatorService availabilityValidator;
    private final NotificationService notificationService;

    private static final List<BookingStatus> ACTIVE_STATUSES = List.of(
            BookingStatus.PENDING,
            BookingStatus.CONFIRMED,
            BookingStatus.COMPLETED
    );

    @Override
    @Transactional
    public BookingDTO create(BookingCreateDTO request) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        if (request.getCompanyId() == null) {
            throw new BusinessException("Company ID is required");
        }

        if (request.getServiceId() == null) {
            throw new BusinessException("Service ID is required");
        }

        if (request.getCustomerId() == null) {
            throw new BusinessException("Customer ID is required");
        }

        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new BusinessException("Start time and end time are required");
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BusinessException("Start time must be before end time");
        }

        if (request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Start time cannot be in the past");
        }

        Company company = companyService.getByIdEntity(request.getCompanyId());

        Service service = serviceService.getByIdEntity(request.getServiceId());

        if (!service.getCompany().getId().equals(request.getCompanyId())) {
            throw new BusinessException("Service does not belong to the specified company");
        }

        Customer customer = customerService.findById(request.getCustomerId());

        if (!customer.getCompany().getId().equals(request.getCompanyId())) {
            throw new BusinessException("Customer does not belong to the specified company");
        }

        Supplier supplier = service.getSupplier();
        checkCreatePermission(currentUser, supplier, request.getCompanyId());

        validateOverlap(
                request.getServiceId(),
                request.getStartTime(),
                request.getEndTime(),
                null
        );

        availabilityValidator.validateBookingAvailability(
                supplier.getId(),
                request.getStartTime(),
                request.getEndTime()
        );

        User createdByUser = userService.findById(currentUser.getUserId());

        Booking booking = new Booking();
        booking.setCompany(company);
        booking.setService(service);
        booking.setCustomer(customer);
        booking.setCreatedByUser(createdByUser);
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setStatus(BookingStatus.PENDING);
        booking.setType(request.getType() != null ? request.getType() : BookingType.APPOINTMENT);
        booking.setName(request.getName());
        booking.setDescription(request.getDescription());

        Booking saved = bookingRepository.save(booking);

        // =========================
        // 🔔 NOTIFICACIONES
        // =========================

        // 1. Notificar al creador de la reserva (el usuario que la creó)
        notificationService.notifyNewBooking(
                currentUser.getUserId(),
                booking.getName(),
                booking.getId()
        );

        // 2. Notificar al supplier (dueño del servicio)
        try {
            User supplierUser = userService.findByPersonId(supplier.getPerson().getId());
            if (supplierUser != null && !supplierUser.getId().equals(currentUser.getUserId())) {
                notificationService.notifyNewBooking(
                        supplierUser.getId(),
                        booking.getName(),
                        booking.getId()
                );
            }
        } catch (Exception e) {
            // Si falla la notificación al supplier, no detener el flujo
            System.err.println("Error sending notification to supplier: " + e.getMessage());
        }

        // 3. Notificar al customer (cliente de la reserva)
        try {
            User customerUser = userService.findByPersonId(customer.getPerson().getId());
            if (customerUser != null && !customerUser.getId().equals(currentUser.getUserId())) {
                notificationService.notifyNewBooking(
                        customerUser.getId(),
                        booking.getName(),
                        booking.getId()
                );
            }
        } catch (Exception e) {
            // Si falla la notificación al customer, no detener el flujo
            System.err.println("Error sending notification to customer: " + e.getMessage());
        }

        return toDTO(saved);
    }

    @Override
    @Transactional
    public BookingDTO patch(Long id, BookingPatchDTO request) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        checkUpdatePermission(currentUser, booking);

        LocalDateTime startTime = request.getStartTime() != null
                ? request.getStartTime()
                : booking.getStartTime();
        LocalDateTime endTime = request.getEndTime() != null
                ? request.getEndTime()
                : booking.getEndTime();

        if (request.getStartTime() != null || request.getEndTime() != null) {
            if (startTime.isAfter(endTime)) {
                throw new BusinessException("Start time must be before end time");
            }

            if (startTime.isBefore(LocalDateTime.now())) {
                throw new BusinessException("Start time cannot be in the past");
            }

            validateOverlap(
                    booking.getService().getId(),
                    startTime,
                    endTime,
                    id
            );

            availabilityValidator.validateBookingAvailability(
                    booking.getService().getSupplier().getId(),
                    startTime,
                    endTime,
                    booking.getId()
            );
        }

        if (request.getServiceId() != null) {
            Service service = serviceService.getByIdEntity(request.getServiceId());

            if (!service.getCompany().getId().equals(booking.getCompany().getId())) {
                throw new BusinessException("Service does not belong to the same company");
            }

            booking.setService(service);
        }

        if (request.getCustomerId() != null) {
            Customer customer = customerService.findById(request.getCustomerId());

            if (!customer.getCompany().getId().equals(booking.getCompany().getId())) {
                throw new BusinessException("Customer does not belong to the same company");
            }

            booking.setCustomer(customer);
        }

        if (request.getStartTime() != null) {
            booking.setStartTime(request.getStartTime());
        }

        if (request.getEndTime() != null) {
            booking.setEndTime(request.getEndTime());
        }

        // 🔔 Guardar el estado anterior antes de actualizar
        BookingStatus previousStatus = booking.getStatus();

        if (request.getStatus() != null) {
            booking.setStatus(request.getStatus());
        }

        if (request.getType() != null) {
            booking.setType(request.getType());
        }

        if (request.getName() != null) {
            booking.setName(request.getName());
        }

        if (request.getDescription() != null) {
            booking.setDescription(request.getDescription());
        }

        Booking saved = bookingRepository.save(booking);

        // =========================
        // 🔔 NOTIFICACIONES POR CAMBIO DE ESTADO
        // =========================

        // Si el estado cambió a CONFIRMED
        if (request.getStatus() != null &&
                request.getStatus() == BookingStatus.CONFIRMED &&
                previousStatus != BookingStatus.CONFIRMED) {

            // Notificar al usuario actual
            notificationService.notifyBookingConfirmed(
                    currentUser.getUserId(),
                    booking.getName(),
                    booking.getId()
            );

            // Notificar al customer (cliente de la reserva)
            try {
                User customerUser = userService.findByPersonId(booking.getCustomer().getPerson().getId());
                if (customerUser != null && !customerUser.getId().equals(currentUser.getUserId())) {
                    notificationService.notifyBookingConfirmed(
                            customerUser.getId(),
                            booking.getName(),
                            booking.getId()
                    );
                }
            } catch (Exception e) {
                System.err.println("Error sending notification to customer: " + e.getMessage());
            }
        }

        // Si el estado cambió a CANCELLED
        if (request.getStatus() != null &&
                request.getStatus() == BookingStatus.CANCELLED &&
                previousStatus != BookingStatus.CANCELLED) {

            // Notificar al usuario actual
            notificationService.notifyBookingCancelled(
                    currentUser.getUserId(),
                    booking.getName(),
                    booking.getId()
            );

            // Notificar al customer (cliente de la reserva)
            try {
                User customerUser = userService.findByPersonId(booking.getCustomer().getPerson().getId());
                if (customerUser != null && !customerUser.getId().equals(currentUser.getUserId())) {
                    notificationService.notifyBookingCancelled(
                            customerUser.getId(),
                            booking.getName(),
                            booking.getId()
                    );
                }
            } catch (Exception e) {
                System.err.println("Error sending notification to customer: " + e.getMessage());
            }
        }

        Service service = booking.getService();
        Supplier supplier = service.getSupplier();
        System.out.println("🔍 Supplier ID: " + supplier.getId());
        System.out.println("🔍 Start: " + request.getStartTime());
        System.out.println("🔍 End: " + request.getEndTime());

        return toDTO(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        checkDeletePermission(currentUser, booking);

        booking.setStatus(BookingStatus.DELETED);
        bookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDTO getById(Long id) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        checkReadPermission(currentUser, booking);

        if (!accessBase.isOwner(currentUser) &&
                !accessBase.isAdmin(currentUser) &&
                booking.getStatus() == BookingStatus.DELETED) {
            throw new NotFoundException("Booking not found");
        }

        return toDTO(booking);
    }


    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getAllByCompany() {

        CurrentUser currentUser = securityHelper.getCurrentUser();
        List<Booking> bookings;

        if (accessBase.isOwner(currentUser)) {
            bookings = bookingRepository.findAll();
        } else if (accessBase.isAdmin(currentUser)) {
            bookings = bookingRepository.findByCompanyId(currentUser.getCompanyId());
            bookings = bookings.stream()
                    .filter(b -> hasReadAccess(currentUser, b))
                    .toList();
        } else {
            List<Supplier> accessibleSuppliers = supplierService.findByUserId(currentUser.getUserId());

            if (accessibleSuppliers.isEmpty()) {
                return List.of();
            }

            List<Long> supplierIds = accessibleSuppliers.stream()
                    .map(Supplier::getId)
                    .collect(Collectors.toList());

            // ✅ Usar findBySupplierIdsWithDetails
            bookings = bookingRepository.findBySupplierIdsWithDetails(supplierIds);
            bookings = bookings.stream()
                    .filter(b -> hasReadAccess(currentUser, b))
                    .toList();
        }

        bookings = bookings.stream()
                .filter(b -> b.getStatus() != BookingStatus.DELETED)
                .collect(Collectors.toList());

        return bookings.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getAllByCustomer(Long customerId) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Customer customer = customerService.findById(customerId);

        if (!accessBase.isOwner(currentUser) &&
                !currentUser.getCompanyId().equals(customer.getCompany().getId())) {
            throw new ForbiddenException("You are not allowed to view bookings from another company");
        }

        List<Booking> bookings = bookingRepository.findByCustomerId(customerId);

        bookings = bookings.stream()
                .filter(b -> hasReadAccess(currentUser, b))
                .toList();

        return bookings.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getByIdEntity(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getAllByService(Long serviceId) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Service service = serviceService.getByIdEntity(serviceId);

        if (!accessBase.isOwner(currentUser) &&
                !currentUser.getCompanyId().equals(service.getCompany().getId())) {
            throw new ForbiddenException("You are not allowed to view bookings from another company");
        }

        List<Booking> bookings = bookingRepository.findByServiceId(serviceId);

        bookings = bookings.stream()
                .filter(b -> hasReadAccess(currentUser, b))
                .toList();

        return bookings.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getAllBySupplier(Long supplierId) {
        System.out.println("🔍 getAllBySupplier - supplierId: " + supplierId);
        CurrentUser currentUser = securityHelper.getCurrentUser();
        System.out.println("🔍 currentUser: " + currentUser);
        System.out.println("🔍 personId: " + currentUser.getPersonId());
        System.out.println("🔍 companyId: " + currentUser.getCompanyId());

        Supplier supplier = supplierService.findById(supplierId);
        System.out.println("🔍 supplier: " + supplier);

        access.requireSupplierAccess(currentUser, supplier);
        System.out.println("✅ Access granted");

        List<Booking> bookings = bookingRepository.findBySupplierId(supplierId);
        System.out.println("🔍 Bookings found: " + bookings.size());

        bookings = bookings.stream()
                .filter(b -> hasReadAccess(currentUser, b))
                .toList();
        System.out.println("🔍 Bookings after filter: " + bookings.size());

        return bookings.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getBySupplierAndDateRange(Long supplierId, LocalDateTime startDate, LocalDateTime endDate) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Supplier supplier = supplierService.findById(supplierId);

        access.requireSupplierAccess(currentUser, supplier);

        List<Booking> bookings = bookingRepository.findBySupplierIdAndDateRange(
                supplierId,
                startDate,
                endDate
        );

        bookings = bookings.stream()
                .filter(b -> hasReadAccess(currentUser, b))
                .toList();

        return bookings.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public void validateOverlap(Long serviceId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId) {

        boolean hasOverlap = bookingRepository.existsOverlapping(
                serviceId,
                startTime,
                endTime,
                excludeId,
                ACTIVE_STATUSES
        );
        if (hasOverlap) {
            throw new BusinessException("Booking overlaps with existing booking for this service");
        }
    }

    private void checkCreatePermission(CurrentUser currentUser, Supplier supplier, Long companyId) {
        if (accessBase.isOwner(currentUser)) {
            return;
        }

        if (accessBase.isAdmin(currentUser)) {
            if (!currentUser.getCompanyId().equals(companyId)) {
                throw new ForbiddenException("You are not allowed to create bookings in another company");
            }
            return;
        }

        if (!currentUser.getCompanyId().equals(companyId)) {
            throw new ForbiddenException("You are not allowed to create bookings in another company");
        }

        Person currentPerson = personService.findById(currentUser.getPersonId());
        Supplier currentSupplier = currentPerson.getSupplier();

        if (currentSupplier != null && currentSupplier.getId().equals(supplier.getId())) {
            return;
        }

        access.requirePermission(currentUser, supplier, Permission.BOOKING_CREATE);
    }

    private void checkReadPermission(CurrentUser currentUser, Booking booking) {
        if (accessBase.isOwner(currentUser)) {
            return;
        }

        if (accessBase.isAdmin(currentUser)) {
            if (!currentUser.getCompanyId().equals(booking.getCompany().getId())) {
                throw new ForbiddenException("You are not allowed to view bookings from another company");
            }
            return;
        }

        if (!currentUser.getCompanyId().equals(booking.getCompany().getId())) {
            throw new ForbiddenException("You are not allowed to view bookings from another company");
        }

        Person currentPerson = personService.findById(currentUser.getPersonId());
        Supplier currentSupplier = currentPerson.getSupplier();
        Supplier bookingSupplier = booking.getService().getSupplier();

        if (currentSupplier != null && currentSupplier.getId().equals(bookingSupplier.getId())) {
            return;
        }

        access.requirePermission(currentUser, bookingSupplier, Permission.BOOKING_READ);
    }

    private void checkUpdatePermission(CurrentUser currentUser, Booking booking) {
        if (accessBase.isOwner(currentUser)) {
            return;
        }

        if (accessBase.isAdmin(currentUser)) {
            if (!currentUser.getCompanyId().equals(booking.getCompany().getId())) {
                throw new ForbiddenException("You are not allowed to update bookings from another company");
            }
            return;
        }

        if (!currentUser.getCompanyId().equals(booking.getCompany().getId())) {
            throw new ForbiddenException("You are not allowed to update bookings from another company");
        }

        Person currentPerson = personService.findById(currentUser.getPersonId());
        Supplier currentSupplier = currentPerson.getSupplier();
        Supplier bookingSupplier = booking.getService().getSupplier();

        if (currentSupplier != null && currentSupplier.getId().equals(bookingSupplier.getId())) {
            return;
        }

        access.requirePermission(currentUser, bookingSupplier, Permission.BOOKING_UPDATE);
    }

    private void checkDeletePermission(CurrentUser currentUser, Booking booking) {
        if (accessBase.isOwner(currentUser)) {
            return;
        }

        if (accessBase.isAdmin(currentUser)) {
            if (!currentUser.getCompanyId().equals(booking.getCompany().getId())) {
                throw new ForbiddenException("You are not allowed to delete bookings from another company");
            }
            return;
        }

        if (!currentUser.getCompanyId().equals(booking.getCompany().getId())) {
            throw new ForbiddenException("You are not allowed to delete bookings from another company");
        }

        Person currentPerson = personService.findById(currentUser.getPersonId());
        Supplier currentSupplier = currentPerson.getSupplier();
        Supplier bookingSupplier = booking.getService().getSupplier();

        if (currentSupplier != null && currentSupplier.getId().equals(bookingSupplier.getId())) {
            return;
        }

        access.requirePermission(currentUser, bookingSupplier, Permission.BOOKING_DELETE);
    }

    private boolean hasReadAccess(CurrentUser currentUser, Booking booking) {
        try {
            checkReadPermission(currentUser, booking);
            return true;
        } catch (ForbiddenException e) {
            return false;
        }
    }

    private BookingDTO toDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();

        dto.setId(booking.getId());
        dto.setCompanyId(booking.getCompany().getId());
        dto.setServiceId(booking.getService().getId());
        dto.setCustomerId(booking.getCustomer().getId());
        dto.setCreatedByUserId(booking.getCreatedByUser().getId());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setStatus(booking.getStatus());
        dto.setType(booking.getType());
        dto.setName(booking.getName());
        dto.setDescription(booking.getDescription());
        dto.setCreatedAt(booking.getCreatedAt());

        return dto;
    }
}