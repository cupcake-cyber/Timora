package com.timora.app.service;

import com.timora.app.dto.booking.BookingCreateDTO;
import com.timora.app.dto.booking.BookingDTO;
import com.timora.app.dto.booking.BookingPatchDTO;
import com.timora.app.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDTO create(BookingCreateDTO request);
    BookingDTO patch(Long id, BookingPatchDTO request);
    void delete(Long id);
    BookingDTO getById(Long id);
    List<BookingDTO> getAllByCompany();
    List<BookingDTO> getAllByCustomer(Long customerId);
    List<BookingDTO> getAllByService(Long serviceId);
    List<BookingDTO> getAllBySupplier(Long supplierId);
    List<BookingDTO> getBySupplierAndDateRange(Long supplierId, LocalDateTime startDate, LocalDateTime endDate);
    Booking getByIdEntity(Long id);
    void validateOverlap(Long serviceId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId);
}