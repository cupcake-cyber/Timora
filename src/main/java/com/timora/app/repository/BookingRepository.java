package com.timora.app.repository;

import com.timora.app.model.Booking;
import com.timora.app.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // =========================
    // 🔥 FETCH COMPLETO (DTO DETAIL)
    // =========================

    @Query("""
        SELECT b FROM Booking b
        JOIN FETCH b.service s
        JOIN FETCH s.supplier sup
        JOIN FETCH sup.person
        JOIN FETCH b.customer c
        JOIN FETCH c.person
        JOIN FETCH b.company
        WHERE b.id = :id
    """)
    Optional<Booking> findByIdFull(@Param("id") Long id);

    // =========================
    // 🔹 COMPANY BOOKINGS
    // =========================

    @Query("""
        SELECT b FROM Booking b
        JOIN FETCH b.service s
        JOIN FETCH b.customer c
        JOIN FETCH c.person
        WHERE b.company.id = :companyId
    """)
    List<Booking> findByCompany(@Param("companyId") Long companyId);

    // =========================
    // 🔹 CUSTOMER BOOKINGS
    // =========================

    @Query("""
        SELECT b FROM Booking b
        JOIN FETCH b.service s
        JOIN FETCH b.customer c
        JOIN FETCH c.person
        WHERE b.customer.id = :customerId
    """)
    List<Booking> findByCustomer(@Param("customerId") Long customerId);

    // =========================
    // 🔹 SUPPLIER BOOKINGS
    // =========================

    @Query("""
        SELECT b FROM Booking b
        JOIN FETCH b.service s
        JOIN FETCH s.supplier sup
        JOIN FETCH sup.person
        WHERE s.supplier.id = :supplierId
    """)
    List<Booking> findBySupplier(@Param("supplierId") Long supplierId);

    // =========================
    // 🔹 STATUS
    // =========================

    @Query("""
        SELECT b FROM Booking b
        WHERE b.status = :status
    """)
    List<Booking> findByStatus(@Param("status") BookingStatus status);

    // =========================
    // 🔹 DATE RANGE
    // =========================

    @Query("""
        SELECT b FROM Booking b
        WHERE b.startTime BETWEEN :start AND :end
    """)
    List<Booking> findBetweenDates(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @EntityGraph(attributePaths = {
            "service",
            "service.supplier",
            "service.supplier.person",
            "customer",
            "customer.person",
            "company"
    })
    @Query("SELECT b FROM Booking b WHERE b.id = :id")
    Optional<Booking> findByIdGraph(@Param("id") Long id);
}