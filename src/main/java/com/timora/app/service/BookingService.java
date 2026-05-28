package com.timora.app.service;

import com.timora.app.dto.*;
import com.timora.app.model.Booking;


import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    Booking create(Booking booking);

    Booking getById(Long id);

    List<Booking> getByCompany(Long companyId);

    List<Booking> getByCustomer(Long customerId);

    List<Booking> getBySupplier(Long supplierId);

    List<Booking> getByStatus(String status);

    List<Booking> getBetweenDates(LocalDateTime start, LocalDateTime end);

    Booking confirm(Long id);

    Booking cancel(Long id);

    Booking complete(Long id);

    void delete(Long id);
}