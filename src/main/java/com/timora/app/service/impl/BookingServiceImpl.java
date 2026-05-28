package com.timora.app.service.impl;

import com.timora.app.dto.*;
import com.timora.app.model.Booking;
import com.timora.app.model.enums.BookingStatus;
import com.timora.app.repository.BookingRepository;
import com.timora.app.service.BookingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;

    public BookingServiceImpl(BookingRepository repository) {
        this.repository = repository;
    }

    // =========================
    // CREATE
    // =========================

    @Override
    public Booking create(Booking booking) {
        booking.setCreatedAt(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);
        return repository.save(booking);
    }

    // =========================
    // READ (DTO)
    // =========================

    @Override
    public BookingDetailsDTO getById(Long id) {

        Booking booking = repository.findByIdFull(id)
                .orElseThrow(() -> new RuntimeException("Booking no encontrado"));

        return toDetailsDTO(booking);
    }

    @Override
    public List<BookingSummaryDTO> getByCompany(Long companyId) {
        return repository.findByCompany(companyId)
                .stream()
                .map(this::toSummaryDTO)
                .toList();
    }

    @Override
    public List<BookingSummaryDTO> getByCustomer(Long customerId) {
        return repository.findByCustomer(customerId)
                .stream()
                .map(this::toSummaryDTO)
                .toList();
    }

    @Override
    public List<BookingSummaryDTO> getBySupplier(Long supplierId) {
        return repository.findBySupplier(supplierId)
                .stream()
                .map(this::toSummaryDTO)
                .toList();
    }

    @Override
    public List<BookingSummaryDTO> getByStatus(String status) {
        return repository.findByStatus(BookingStatus.valueOf(status))
                .stream()
                .map(this::toSummaryDTO)
                .toList();
    }

    @Override
    public List<BookingSummaryDTO> getBetweenDates(LocalDateTime start, LocalDateTime end) {
        return repository.findBetweenDates(start, end)
                .stream()
                .map(this::toSummaryDTO)
                .toList();
    }

    // =========================
    // STATE CHANGES
    // =========================

    @Override
    public Booking confirm(Long id) {
        Booking booking = findEntity(id);
        booking.setStatus(BookingStatus.CONFIRMED);
        return repository.save(booking);
    }

    @Override
    public Booking cancel(Long id) {
        Booking booking = findEntity(id);
        booking.setStatus(BookingStatus.CANCELLED);
        return repository.save(booking);
    }

    @Override
    public Booking complete(Long id) {
        Booking booking = findEntity(id);
        booking.setStatus(BookingStatus.COMPLETED);
        return repository.save(booking);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // =========================
    // INTERNAL
    // =========================

    private Booking findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking no encontrado"));
    }

    private BookingDetailsDTO toDetailsDTO(Booking b) {
        return new BookingDetailsDTO(
                b.getId(),
                b.getService().getName(),
                b.getCustomer().getPerson().getFirstName() + " " +
                        b.getCustomer().getPerson().getLastName(),
                b.getService().getSupplier().getPerson().getFirstName(),
                b.getStartTime(),
                b.getEndTime(),
                b.getStatus(),
                b.getDescription()
        );
    }

    private BookingSummaryDTO toSummaryDTO(Booking b) {
        return new BookingSummaryDTO(
                b.getId(),
                b.getService().getName(),
                b.getCustomer().getPerson().getFirstName(),
                b.getStartTime(),
                b.getStatus()
        );
    }

    private BookingAdminDTO toAdminDTO(Booking b) {
        return new BookingAdminDTO(
                b.getId(),
                b.getCompany().getId(),
                b.getService().getId(),
                b.getCustomer().getId(),
                b.getStatus(),
                b.getCreatedAt()
        );
    }
}