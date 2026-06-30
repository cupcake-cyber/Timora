//package com.timora.app.service.impl;
//
//import com.timora.app.dto.*;
//import com.timora.app.model.*;
//import com.timora.app.model.enums.BookingStatus;
//import com.timora.app.model.enums.BookingType;
//import com.timora.app.model.enums.AvailabilityStatus;
//import com.timora.app.repository.*;
//import com.timora.app.service.BookingService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class BookingServiceImpl implements BookingService {
//
//    private final BookingRepository repository;
//    private final ServiceRepository serviceRepository;
//    private final CustomerRepository customerRepository;
//    private final UserRepository userRepository;
//    private final SupplierRepository supplierRepository;
//    private final AvailabilityRepository availabilityRepository;
//
//    // =========================================================
//    // CREATE
//    // =========================================================
//    @Override
//    public BookingDetailsDTO create(BookingCreateDTO dto) {
//
//        User user = getCurrentUser();
//        Supplier supplier = getSupplierFromUser(user);
//
//        Customer customer = customerRepository.findById(dto.getCustomerId())
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//
//        com.timora.app.model.Service service = serviceRepository.findById(dto.getServiceId())
//                .orElseThrow(() -> new RuntimeException("Service not found"));
//
//        // 1. ownership del servicio
//        if (!service.getSupplier().getId().equals(supplier.getId())) {
//            throw new RuntimeException("Service does not belong to supplier");
//        }
//
//        // 2. misma company
//        if (!customer.getCompany().getId().equals(user.getCompany().getId())) {
//            throw new RuntimeException("Customer not in same company");
//        }
//
//        // 3. availability
//        validateAvailability(supplier, dto.getStartTime(), dto.getEndTime());
//
//        // 4. overlap
//        if (repository.existsOverlap(
//                supplier.getId(),
//                dto.getStartTime(),
//                dto.getEndTime()
//        )) {
//            throw new RuntimeException("Time slot already booked");
//        }
//
//        Booking booking = new Booking();
//        booking.setCompany(user.getCompany());
//        booking.setService(service);
//        booking.setCustomer(customer);
//        booking.setCreatedByUser(user);
//
//        booking.setStartTime(dto.getStartTime());
//        booking.setEndTime(dto.getEndTime());
//
//        booking.setName(dto.getName());
//        booking.setDescription(dto.getDescription());
//
//        booking.setStatus(BookingStatus.PENDING);
//        booking.setType(BookingType.APPOINTMENT);
//
//        repository.save(booking);
//
//        return toDetailsDTO(booking);
//    }
//
//    // =========================================================
//    // READ BY ID
//    // =========================================================
//    @Override
//    public BookingDetailsDTO getById(Long id) {
//
//        Booking booking = repository.findByIdFull(id)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//        return toDetailsDTO(booking);
//    }
//
//    // =========================================================
//    // SUPPLIER BOOKINGS
//    // =========================================================
//    @Override
//    public List<BookingSummaryDTO> getMyBookings() {
//
//        User user = getCurrentUser();
//        Supplier supplier = getSupplierFromUser(user);
//
//        return repository.findBySupplier(supplier.getId())
//                .stream()
//                .map(this::toSummaryDTO)
//                .toList();
//    }
//
//    // =========================================================
//    // OTHER LISTS
//    // =========================================================
//    @Override
//    public List<BookingSummaryDTO> getByCompany(Long companyId) {
//        return repository.findByCompany(companyId)
//                .stream()
//                .map(this::toSummaryDTO)
//                .toList();
//    }
//
//    @Override
//    public List<BookingSummaryDTO> getByCustomer(Long customerId) {
//        return repository.findByCustomer(customerId)
//                .stream()
//                .map(this::toSummaryDTO)
//                .toList();
//    }
//
//    // =========================================================
//    // STATUS
//    // =========================================================
//    @Override
//    public BookingDetailsDTO confirm(Long id) {
//        return updateStatus(id, BookingStatus.CONFIRMED);
//    }
//
//    @Override
//    public BookingDetailsDTO cancel(Long id) {
//        return updateStatus(id, BookingStatus.CANCELLED);
//    }
//
//    @Override
//    public BookingDetailsDTO complete(Long id) {
//        return updateStatus(id, BookingStatus.COMPLETED);
//    }
//
//    @Override
//    public void delete(Long id) {
//        repository.deleteById(id);
//    }
//
//    private BookingDetailsDTO updateStatus(Long id, BookingStatus status) {
//
//        Booking booking = findEntity(id);
//        booking.setStatus(status);
//
//        repository.save(booking);
//
//        return toDetailsDTO(booking);
//    }
//
//    // =========================================================
//    // AVAILABILITY RULE
//    // =========================================================
//    private void validateAvailability(Supplier supplier,
//                                      LocalDateTime start,
//                                      LocalDateTime end) {
//
//        List<Availability> availabilities =
//                availabilityRepository.findByCompanyIdAndSupplierIdAndStatus(
//                        supplier.getCompany().getId(),
//                        supplier.getId(),
//                        AvailabilityStatus.ACTIVE
//                );
//
//        boolean valid = availabilities.stream().anyMatch(a -> {
//
//            LocalDateTime slotStart = LocalDateTime.of(
//                    start.toLocalDate(),
//                    a.getStartTime()
//            );
//
//            LocalDateTime slotEnd = LocalDateTime.of(
//                    start.toLocalDate(),
//                    a.getEndTime()
//            );
//
//            return !start.isBefore(slotStart) && !end.isAfter(slotEnd);
//        });
//
//        if (!valid) {
//            throw new RuntimeException("Outside supplier availability");
//        }
//    }
//
//    // =========================================================
//    // HELPERS
//    // =========================================================
//    private User getCurrentUser() {
//        String email = SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getName();
//
//        return userRepository.findByLoginEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }
//
//    private Supplier getSupplierFromUser(User user) {
//        return supplierRepository.findByUserIdAndCompanyId(
//                user.getId(),
//                user.getCompany().getId()
//        ).orElseThrow(() -> new RuntimeException("User is not supplier"));
//    }
//
//    private Booking findEntity(Long id) {
//        return repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//    }
//
//    // =========================================================
//    // MAPPERS
//    // =========================================================
//    private BookingDetailsDTO toDetailsDTO(Booking b) {
//        return new BookingDetailsDTO(
//                b.getId(),
//                b.getService().getName(),
//                b.getCustomer().getPerson().getFirstName(),
//                b.getService().getSupplier().getPerson().getFirstName(),
//                b.getStartTime(),
//                b.getEndTime(),
//                b.getStatus(),
//                b.getDescription()
//        );
//    }
//
//    private BookingSummaryDTO toSummaryDTO(Booking b) {
//        return new BookingSummaryDTO(
//                b.getId(),
//                b.getService().getName(),
//                b.getCustomer().getPerson().getFirstName(),
//                b.getStartTime(),
//                b.getStatus()
//        );
//    }
//}