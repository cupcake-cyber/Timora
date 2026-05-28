package com.timora.app.service;

import com.timora.app.dto.BookingAdminDTO;
import com.timora.app.dto.BookingDetailsDTO;
import com.timora.app.dto.BookingSummaryDTO;
import com.timora.app.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    // CREATE
    Booking create(Booking booking);

    // READ (DTO)
    BookingDetailsDTO getById(Long id);

    List<BookingSummaryDTO> getByCompany(Long companyId);

    List<BookingSummaryDTO> getByCustomer(Long customerId);

    List<BookingSummaryDTO> getBySupplier(Long supplierId);

    List<BookingSummaryDTO> getByStatus(String status);

    List<BookingSummaryDTO> getBetweenDates(LocalDateTime start, LocalDateTime end);

    // STATE CHANGES
    Booking confirm(Long id);

    Booking cancel(Long id);

    Booking complete(Long id);

    void delete(Long id);
}