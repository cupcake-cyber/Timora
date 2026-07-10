package com.timora.app.repository;

import com.timora.app.model.Booking;
import com.timora.app.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Busca bookings de una compañía
     */
    List<Booking> findByCompanyId(Long companyId);

    /**
     * Busca bookings de un cliente
     */
    List<Booking> findByCustomerId(Long customerId);

    /**
     * Busca bookings de un servicio
     */
    List<Booking> findByServiceId(Long serviceId);

    /**
     * Busca bookings por estado
     */
    List<Booking> findByStatus(BookingStatus status);

    /**
     * Busca bookings de una compañía por estado
     */
    List<Booking> findByCompanyIdAndStatus(Long companyId, BookingStatus status);

    /**
     * Busca bookings que se solapan en un rango de tiempo para un servicio específico
     */
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b WHERE b.service.id = :serviceId " +
            "AND b.status IN :activeStatuses " +
            "AND b.startTime < :endTime " +
            "AND b.endTime > :startTime " +
            "AND (:excludeId IS NULL OR b.id != :excludeId)")
    boolean existsOverlapping(
            @Param("serviceId") Long serviceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") Long excludeId,
            @Param("activeStatuses") List<BookingStatus> activeStatuses
    );

    /**
     * Verifica si un servicio tiene bookings en un rango de tiempo (para validar disponibilidad)
     */
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b WHERE b.service.id = :serviceId " +
            "AND b.status IN :activeStatuses " +
            "AND b.startTime < :endTime " +
            "AND b.endTime > :startTime")
    boolean hasOverlappingBookings(
            @Param("serviceId") Long serviceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("activeStatuses") List<BookingStatus> activeStatuses
    );

    /**
     * Obtiene todos los bookings de un proveedor (supplier) a través de sus servicios
     */
    @Query("SELECT b FROM Booking b WHERE b.service.supplier.id = :supplierId")
    List<Booking> findBySupplierId(@Param("supplierId") Long supplierId);

    /**
     * Obtiene bookings de un proveedor en un rango de fechas
     */
    @Query("SELECT b FROM Booking b WHERE b.service.supplier.id = :supplierId " +
            "AND b.startTime >= :startDate " +
            "AND b.endTime <= :endDate")
    List<Booking> findBySupplierIdAndDateRange(
            @Param("supplierId") Long supplierId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}