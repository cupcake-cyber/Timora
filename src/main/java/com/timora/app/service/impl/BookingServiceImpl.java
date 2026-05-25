package com.timora.app.service.impl;

import com.timora.app.model.Booking;
import com.timora.app.model.enums.BookingStatus;
import com.timora.app.repository.BookingRepository;
import com.timora.app.service.BookingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Booking save(Booking booking) {
        booking.setCreatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(Long id, Booking updated) {
        return bookingRepository.findById(id).map(booking -> {

            booking.setCompany(updated.getCompany());
            booking.setService(updated.getService());
            booking.setCustomer(updated.getCustomer());
            booking.setCreatedByUser(updated.getCreatedByUser());

            booking.setStartTime(updated.getStartTime());
            booking.setEndTime(updated.getEndTime());
            booking.setStatus(updated.getStatus());
            booking.setType(updated.getType());
            booking.setName(updated.getName());
            booking.setDescription(updated.getDescription());

            return bookingRepository.save(booking);

        }).orElseThrow(() -> new RuntimeException("Booking no encontrado con id: " + id));
    }

    @Override
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public Booking confirm(Long id) {
        return bookingRepository.findById(id).map(booking -> {
            booking.setStatus(BookingStatus.CONFIRMED);
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking no encontrado"));
    }

    @Override
    public Booking cancel(Long id) {
        return bookingRepository.findById(id).map(booking -> {
            booking.setStatus(BookingStatus.CANCELLED);
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking no encontrado"));
    }

    @Override
    public List<Booking> findByCustomerId(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> findByServiceId(Long serviceId) {
        return bookingRepository.findByServiceId(serviceId);
    }

    @Override
    public List<Booking> findByCompanyId(Long companyId) {
        return bookingRepository.findByCompanyId(companyId);
    }
}