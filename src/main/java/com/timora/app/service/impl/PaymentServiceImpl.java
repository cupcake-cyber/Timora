package com.timora.app.service.impl;

import com.timora.app.dto.payment.PaymentCreateDTO;
import com.timora.app.dto.payment.PaymentDTO;
import com.timora.app.dto.payment.PaymentPatchDTO;
import com.timora.app.dto.security.CurrentUser;
import com.timora.app.exception.BusinessException;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.exception.NotFoundException;
import com.timora.app.model.Booking;
import com.timora.app.model.Company;
import com.timora.app.model.Payment;
import com.timora.app.model.Person;
import com.timora.app.model.Supplier;
import com.timora.app.model.enums.PaymentMethod;
import com.timora.app.model.enums.PaymentStatus;
import com.timora.app.model.enums.Permission;
import com.timora.app.repository.BookingRepository;
import com.timora.app.repository.PaymentRepository;
import com.timora.app.security.AccessControlBaseService;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CompanyService companyService;
    private final BookingService bookingService;
    private final PersonService personService;
    private final SecurityHelper securityHelper;
    private final AccessControlService access;
    private final AccessControlBaseService accessBase;
    private final SupplierService supplierService;
    private final BookingRepository bookingRepository;

    // Estados activos para validación de pago único por booking
    private static final List<PaymentStatus> ACTIVE_PAYMENT_STATUSES = List.of(
            PaymentStatus.PENDING,
            PaymentStatus.PAID,
            PaymentStatus.PARTIALLY_PAID
    );

    // =========================
    // CREATE
    // =========================

    @Override
    @Transactional
    public PaymentDTO create(PaymentCreateDTO request) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        // =========================
        // 1. VALIDACIONES BÁSICAS
        // =========================

        if (request.getCompanyId() == null) {
            throw new BusinessException("Company ID is required");
        }

        if (request.getBookingId() == null) {
            throw new BusinessException("Booking ID is required");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Amount must be greater than 0");
        }

        // =========================
        // 2. VALIDAR ENTIDADES (usando servicios)
        // =========================

        Company company = companyService.getByIdEntity(request.getCompanyId());

        Booking booking = bookingService.getByIdEntity(request.getBookingId());

        // Verificar que el booking pertenece a la compañía
        if (!booking.getCompany().getId().equals(request.getCompanyId())) {
            throw new BusinessException("Booking does not belong to the specified company");
        }

        // Verificar que no exista un pago activo para este booking
        if (paymentRepository.existsByBookingIdAndStatusIn(
                request.getBookingId(),
                ACTIVE_PAYMENT_STATUSES
        )) {
            throw new BusinessException("This booking already has an active payment");
        }

        // =========================
        // 3. 🔐 CONTROL DE ACCESO
        // =========================

        Supplier supplier = booking.getService().getSupplier();
        checkCreatePermission(currentUser, supplier, request.getCompanyId());

        // =========================
        // 4. CREAR PAGO
        // =========================

        Payment payment = new Payment();
        payment.setCompany(company);
        payment.setBooking(booking);
        payment.setAmount(request.getAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setMethod(request.getMethod() != null ? request.getMethod() : PaymentMethod.CASH);

        Payment saved = paymentRepository.save(payment);

        return toDTO(saved);
    }

    // =========================
    // PATCH (UPDATE)
    // =========================

    @Override
    @Transactional
    public PaymentDTO patch(Long id, PaymentPatchDTO request) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        // =========================
        // 🔐 CONTROL DE ACCESO
        // =========================

        checkUpdatePermission(currentUser, payment);

        // =========================
        // VALIDACIONES
        // =========================

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Amount must be greater than 0");
        }

        // =========================
        // ACTUALIZAR CAMPOS
        // =========================

        if (request.getAmount() != null) {
            payment.setAmount(request.getAmount());
        }

        if (request.getStatus() != null) {
            payment.setStatus(request.getStatus());
        }

        if (request.getMethod() != null) {
            payment.setMethod(request.getMethod());
        }

        Payment saved = paymentRepository.save(payment);

        return toDTO(saved);
    }

    // =========================
    // DELETE (Soft Delete)
    // =========================

    @Override
    @Transactional
    public void delete(Long id) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        // =========================
        // 🔐 CONTROL DE ACCESO
        // =========================

        checkDeletePermission(currentUser, payment);

        // Soft delete - cambiar estado a DELETED
        payment.setStatus(PaymentStatus.DELETED);
        paymentRepository.save(payment);
    }

    // =========================
    // GET BY ID
    // =========================

    @Override
    @Transactional(readOnly = true)
    public PaymentDTO getById(Long id) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        // =========================
        // 🔐 CONTROL DE ACCESO
        // =========================

        checkReadPermission(currentUser, payment);

        return toDTO(payment);
    }

    // =========================
    // GET ALL BY COMPANY
    // =========================


    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getAllByCompany() {

        CurrentUser currentUser = securityHelper.getCurrentUser();
        List<Payment> payments;

        if (accessBase.isOwner(currentUser)) {
            payments = paymentRepository.findAll();
        } else if (accessBase.isAdmin(currentUser)) {
            payments = paymentRepository.findByCompanyId(currentUser.getCompanyId());
            payments = payments.stream()
                    .filter(p -> hasReadAccess(currentUser, p))
                    .toList();
        } else {
            // USER: Ve pagos de bookings donde tiene permisos
            List<Supplier> accessibleSuppliers = supplierService.findByUserId(currentUser.getUserId());

            if (accessibleSuppliers.isEmpty()) {
                return List.of();
            }

            List<Long> supplierIds = accessibleSuppliers.stream()
                    .map(Supplier::getId)
                    .collect(Collectors.toList());

            // ✅ USAR findBySupplierIdsWithDetails en lugar de findBySupplierIds
            List<Booking> bookings = bookingRepository.findBySupplierIdsWithDetails(supplierIds);

            if (bookings.isEmpty()) {
                return List.of();
            }

            List<Long> bookingIds = bookings.stream()
                    .map(Booking::getId)
                    .collect(Collectors.toList());

            payments = paymentRepository.findByBookingIds(bookingIds);

            payments = payments.stream()
                    .filter(p -> hasReadAccess(currentUser, p))
                    .toList();
        }

        // Filtrar pagos eliminados
        payments = payments.stream()
                .filter(p -> p.getStatus() != PaymentStatus.DELETED)
                .toList();

        return payments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    // =========================
    // GET BY BOOKING ID
    // =========================

    @Override
    @Transactional(readOnly = true)
    public PaymentDTO getByBookingId(Long bookingId) {

        CurrentUser currentUser = securityHelper.getCurrentUser();


        Booking booking = bookingService.getByIdEntity(bookingId);

        if(accessBase.isOwner(currentUser)) {
            if(!accessBase.isSameCompany(currentUser,booking.getCompany().getId())){
                throw new ForbiddenException("You are not allowed to perform this action");
            }
        }

        Payment payment = paymentRepository.findByBookingId(bookingId);

        if (payment == null) {
            throw new NotFoundException("Payment not found for booking with id: " + bookingId);
        }

        // Verificar permisos de lectura
        checkReadPermission(currentUser, payment);

        return toDTO(payment);
    }

    // =========================
    // GET BY STATUS
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getByStatus(PaymentStatus status) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        List<Payment> payments;

        if (accessBase.isOwner(currentUser)) {
            payments = paymentRepository.findByStatus(status);
        } else {
            payments = paymentRepository.findByCompanyIdAndStatus(
                    currentUser.getCompanyId(),
                    status
            );

            // Filtrar por permisos de lectura
            payments = payments.stream()
                    .filter(p -> hasReadAccess(currentUser, p))
                    .toList();
        }

        return payments.stream()
                .map(this::toDTO)
                .toList();
    }

    // =========================
    // MÉTODOS DE PERMISOS
    // =========================

    private void checkCreatePermission(CurrentUser currentUser, Supplier supplier, Long companyId) {
        // OWNER: Acceso total
        if (accessBase.isOwner(currentUser)) {
            return;
        }

        // ADMIN: Solo dentro de su compañía
        if (accessBase.isAdmin(currentUser)) {
            if (!currentUser.getCompanyId().equals(companyId)) {
                throw new ForbiddenException(
                        "You are not allowed to create payments in another company"
                );
            }
            return;
        }

        // USER: Necesita ser supplier o tener permiso
        if (!currentUser.getCompanyId().equals(companyId)) {
            throw new ForbiddenException(
                    "You are not allowed to create payments in another company"
            );
        }

        Person currentPerson = personService.findById(currentUser.getPersonId());
        Supplier currentSupplier = currentPerson.getSupplier();

        // CASO A: El usuario ES el supplier
        if (currentSupplier != null &&
                currentSupplier.getId().equals(supplier.getId())) {
            return;
        }

        // CASO B: Tiene permiso BOOKING_CREATE para este supplier
        access.requirePermission(currentUser, supplier, Permission.BOOKING_CREATE);
    }

    private void checkReadPermission(CurrentUser currentUser, Payment payment) {
        // OWNER: Acceso total
        if (accessBase.isOwner(currentUser)) {
            return;
        }

        // ADMIN: Solo dentro de su compañía
        if (accessBase.isAdmin(currentUser)) {
            if (!currentUser.getCompanyId().equals(payment.getCompany().getId())) {
                throw new ForbiddenException(
                        "You are not allowed to view payments from another company"
                );
            }
            return;
        }

        // USER: Necesita ser supplier o tener permiso
        if (!currentUser.getCompanyId().equals(payment.getCompany().getId())) {
            throw new ForbiddenException(
                    "You are not allowed to view payments from another company"
            );
        }

        Person currentPerson = personService.findById(currentUser.getPersonId());
        Supplier currentSupplier = currentPerson.getSupplier();
        Supplier bookingSupplier = payment.getBooking().getService().getSupplier();

        // CASO A: El usuario ES el supplier del booking
        if (currentSupplier != null &&
                currentSupplier.getId().equals(bookingSupplier.getId())) {
            return;
        }

        // CASO B: Tiene permiso BOOKING_READ para este supplier
        access.requirePermission(
                currentUser,
                bookingSupplier,
                Permission.BOOKING_READ
        );
    }

    private void checkUpdatePermission(CurrentUser currentUser, Payment payment) {
        // OWNER: Acceso total
        if (accessBase.isOwner(currentUser)) {
            return;
        }

        // ADMIN: Solo dentro de su compañía
        if (accessBase.isAdmin(currentUser)) {
            if (!currentUser.getCompanyId().equals(payment.getCompany().getId())) {
                throw new ForbiddenException(
                        "You are not allowed to update payments from another company"
                );
            }
            return;
        }

        // USER: Necesita ser supplier o tener permiso
        if (!currentUser.getCompanyId().equals(payment.getCompany().getId())) {
            throw new ForbiddenException(
                    "You are not allowed to update payments from another company"
            );
        }

        Person currentPerson = personService.findById(currentUser.getPersonId());
        Supplier currentSupplier = currentPerson.getSupplier();
        Supplier bookingSupplier = payment.getBooking().getService().getSupplier();

        // CASO A: El usuario ES el supplier del booking
        if (currentSupplier != null &&
                currentSupplier.getId().equals(bookingSupplier.getId())) {
            return;
        }

        // CASO B: Tiene permiso BOOKING_UPDATE para este supplier
        access.requirePermission(
                currentUser,
                bookingSupplier,
                Permission.BOOKING_UPDATE
        );
    }

    private void checkDeletePermission(CurrentUser currentUser, Payment payment) {
        // OWNER: Acceso total
        if (accessBase.isOwner(currentUser)) {
            return;
        }

        // ADMIN: Solo dentro de su compañía
        if (accessBase.isAdmin(currentUser)) {
            if (!currentUser.getCompanyId().equals(payment.getCompany().getId())) {
                throw new ForbiddenException(
                        "You are not allowed to delete payments from another company"
                );
            }
            return;
        }

        // USER: Necesita ser supplier o tener permiso
        if (!currentUser.getCompanyId().equals(payment.getCompany().getId())) {
            throw new ForbiddenException(
                    "You are not allowed to delete payments from another company"
            );
        }

        Person currentPerson = personService.findById(currentUser.getPersonId());
        Supplier currentSupplier = currentPerson.getSupplier();
        Supplier bookingSupplier = payment.getBooking().getService().getSupplier();

        // CASO A: El usuario ES el supplier del booking
        if (currentSupplier != null &&
                currentSupplier.getId().equals(bookingSupplier.getId())) {
            return;
        }

        // CASO B: Tiene permiso BOOKING_DELETE para este supplier
        access.requirePermission(
                currentUser,
                bookingSupplier,
                Permission.BOOKING_DELETE
        );
    }

    private boolean hasReadAccess(CurrentUser currentUser, Payment payment) {
        try {
            // Asegurar que las relaciones estén cargadas
            if (payment.getBooking() != null && payment.getBooking().getService() != null) {
                // Ya están cargadas con JOIN FETCH
                checkReadPermission(currentUser, payment);
                return true;
            }
            return false;
        } catch (ForbiddenException e) {
            return false;
        } catch (Exception e) {
            // Si hay LazyInitializationException, retornar false
            return false;
        }
    }

    // =========================
    // TO DTO
    // =========================

    private PaymentDTO toDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();

        dto.setId(payment.getId());
        dto.setCompanyId(payment.getCompany().getId());
        dto.setBookingId(payment.getBooking().getId());
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus());
        dto.setMethod(payment.getMethod());
        dto.setCreatedAt(payment.getCreatedAt());

        return dto;
    }
}