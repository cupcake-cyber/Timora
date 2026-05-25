package com.timora.app.repository;

import com.timora.app.model.Booking;
import com.timora.app.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {


    List<Booking> findByStatus(BookingStatus status);


    List<Booking> findByCustomerId(Long customerId);


    List<Booking> findByCreatedByUserId(Long userId);


    List<Booking> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);


    List<Booking> findByServiceSupplierId(Long supplierId);


    List<Booking> findByCompanyId(Long companyId);

    List<Booking> findByServiceId(Long serviceId);
}