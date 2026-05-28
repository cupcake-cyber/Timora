package com.timora.app.service.impl;

import com.timora.app.dto.*;
import com.timora.app.model.*;
import com.timora.app.model.enums.BookingStatus;
import com.timora.app.repository.*;
import com.timora.app.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;

    public BookingServiceImpl(BookingRepository repository) {
        this.repository = repository;
    }

    @Override
    public Booking create(Booking booking) {
        booking.setCreatedAt(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);
        return repository.save(booking);
    }

    @Override
    public Booking getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking no encontrado"));
    }

    @Override
    public List<Booking> getByCompany(Long companyId) {
        return repository.findByCompany(companyId);
    }

    @Override
    public List<Booking> getByCustomer(Long customerId) {
        return repository.findByCustomer(customerId);
    }

    @Override
    public List<Booking> getBySupplier(Long supplierId) {
        return repository.findBySupplier(supplierId);
    }

    @Override
    public List<Booking> getByStatus(String status) {
        return repository.findByStatus(BookingStatus.valueOf(status));
    }

    @Override
    public List<Booking> getBetweenDates(LocalDateTime start, LocalDateTime end) {
        return repository.findBetweenDates(start, end);
    }

    @Override
    public Booking confirm(Long id) {
        Booking booking = getById(id);
        booking.setStatus(BookingStatus.CONFIRMED);
        return repository.save(booking);
    }

    @Override
    public Booking cancel(Long id) {
        Booking booking = getById(id);
        booking.setStatus(BookingStatus.CANCELLED);
        return repository.save(booking);
    }

    @Override
    public Booking complete(Long id) {
        Booking booking = getById(id);
        booking.setStatus(BookingStatus.COMPLETED);
        return repository.save(booking);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}