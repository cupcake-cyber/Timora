package com.timora.app.repository;

import com.timora.app.model.Booking;
import com.timora.app.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // 🔹 JPQL: bookings por empresa
    @Query("SELECT b FROM Booking b WHERE b.company.id = :companyId")
    List<Booking> findByCompany(Long companyId);

    // 🔹 JPQL: bookings por cliente
    @Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId")
    List<Booking> findByCustomer(Long customerId);

    // 🔹 JPQL: bookings por supplier (a través de service)
    @Query("SELECT b FROM Booking b WHERE b.service.supplier.id = :supplierId")
    List<Booking> findBySupplier(Long supplierId);

    // 🔹 JPQL: rango de fechas
    @Query("SELECT b FROM Booking b WHERE b.startTime BETWEEN :start AND :end")
    List<Booking> findBetweenDates(LocalDateTime start, LocalDateTime end);

    // 🔹 JPQL: por estado
    @Query("SELECT b FROM Booking b WHERE b.status = :status")
    List<Booking> findByStatus(BookingStatus status);
}