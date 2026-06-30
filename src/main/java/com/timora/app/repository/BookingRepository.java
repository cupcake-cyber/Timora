//package com.timora.app.repository;
//
//import com.timora.app.model.Booking;
//import com.timora.app.model.enums.BookingStatus;
//import org.springframework.data.jpa.repository.EntityGraph;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//public interface BookingRepository extends JpaRepository<Booking, Long> {
//
//    // =========================================================
//    // FETCH FULL DETAIL
//    // =========================================================
//    @Query("""
//        SELECT b FROM Booking b
//        JOIN FETCH b.service s
//        JOIN FETCH s.supplier sup
//        JOIN FETCH sup.person
//        JOIN FETCH b.customer c
//        JOIN FETCH c.person
//        JOIN FETCH b.company
//        WHERE b.id = :id
//    """)
//    Optional<Booking> findByIdFull(@Param("id") Long id);
//
//    // =========================================================
//    // COMPANY BOOKINGS
//    // =========================================================
//    @Query("""
//        SELECT b FROM Booking b
//        JOIN FETCH b.service s
//        JOIN FETCH b.customer c
//        JOIN FETCH c.person
//        WHERE b.company.id = :companyId
//    """)
//    List<Booking> findByCompany(@Param("companyId") Long companyId);
//
//    // =========================================================
//    // CUSTOMER BOOKINGS
//    // =========================================================
//    @Query("""
//        SELECT b FROM Booking b
//        JOIN FETCH b.service s
//        JOIN FETCH b.customer c
//        JOIN FETCH c.person
//        WHERE b.customer.id = :customerId
//    """)
//    List<Booking> findByCustomer(@Param("customerId") Long customerId);
//
//    // =========================================================
//    // SUPPLIER BOOKINGS
//    // =========================================================
//    @Query("""
//        SELECT b FROM Booking b
//        JOIN FETCH b.service s
//        JOIN FETCH s.supplier sup
//        JOIN FETCH sup.person
//        WHERE s.supplier.id = :supplierId
//    """)
//    List<Booking> findBySupplier(@Param("supplierId") Long supplierId);
//
//    // 👉 FIX PARA TU SERVICE
//    List<Booking> findByServiceSupplierId(Long supplierId);
//
//    // =========================================================
//    // STATUS
//    // =========================================================
//    @Query("""
//        SELECT b FROM Booking b
//        WHERE b.status = :status
//    """)
//    List<Booking> findByStatus(@Param("status") BookingStatus status);
//
//    // =========================================================
//    // DATE RANGE
//    // =========================================================
//    @Query("""
//        SELECT b FROM Booking b
//        WHERE b.startTime BETWEEN :start AND :end
//    """)
//    List<Booking> findBetweenDates(
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end
//    );
//
//    // =========================================================
//    // OVERLAP CHECK (CRÍTICO)
//    // =========================================================
//    @Query("""
//        SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
//        FROM Booking b
//        WHERE b.service.supplier.id = :supplierId
//          AND b.status <> com.timora.app.model.enums.BookingStatus.CANCELLED
//          AND b.startTime < :end
//          AND b.endTime > :start
//    """)
//    boolean existsOverlap(
//            @Param("supplierId") Long supplierId,
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end
//    );
//
//    // =========================================================
//    // ENTITY GRAPH (OPTIONAL OPTIMIZED FETCH)
//    // =========================================================
//    @EntityGraph(attributePaths = {
//            "service",
//            "service.supplier",
//            "service.supplier.person",
//            "customer",
//            "customer.person",
//            "company"
//    })
//    @Query("SELECT b FROM Booking b WHERE b.id = :id")
//    Optional<Booking> findByIdGraph(@Param("id") Long id);
//
//    @Query("""
//    SELECT COUNT(a) > 0
//    FROM Availability a
//    WHERE a.company.id = :companyId
//      AND a.supplier.id = :supplierId
//      AND a.status = com.timora.app.model.enums.AvailabilityStatus.ACTIVE
//      AND a.startDate <= :date
//      AND a.endDate >= :date
//      AND a.startTime <= :startTime
//      AND a.endTime >= :endTime
//""")
//    boolean existsValidSlot(
//            @Param("companyId") Long companyId,
//            @Param("supplierId") Long supplierId,
//            @Param("date") java.time.LocalDate date,
//            @Param("startTime") java.time.LocalTime startTime,
//            @Param("endTime") java.time.LocalTime endTime
//    );
//
//
//}