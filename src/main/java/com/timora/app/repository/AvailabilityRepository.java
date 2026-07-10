package com.timora.app.repository;

import com.timora.app.model.Availability;
import com.timora.app.model.enums.AvailabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    /**
     * Busca todas las disponibilidades de un supplier (incluyendo INACTIVE)
     */
    List<Availability> findBySupplierId(Long supplierId);

    /**
     * Busca todas las disponibilidades de una compañía (incluyendo INACTIVE)
     */
    List<Availability> findByCompanyId(Long companyId);

    /**
     * Busca disponibilidades activas de un supplier
     */
    List<Availability> findBySupplierIdAndStatus(Long supplierId, AvailabilityStatus status);

    /**
     * Busca disponibilidades activas de una compañía
     */
    List<Availability> findByCompanyIdAndStatus(Long companyId, AvailabilityStatus status);

    /**
     * Busca disponibilidades de un supplier que cubren una fecha específica
     */
    @Query("SELECT a FROM Availability a WHERE a.supplier.id = :supplierId " +
            "AND a.status = :status " +
            "AND a.startDate <= :date " +
            "AND (a.endDate IS NULL OR a.endDate >= :date)")
    List<Availability> findBySupplierIdAndDate(
            @Param("supplierId") Long supplierId,
            @Param("date") LocalDate date,
            @Param("status") AvailabilityStatus status
    );

    /**
     * Verifica si existe una disponibilidad que se solape con un rango de fechas
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Availability a WHERE a.supplier.id = :supplierId " +
            "AND a.status = :status " +
            "AND a.startDate <= :endDate " +
            "AND (a.endDate IS NULL OR a.endDate >= :startDate) " +
            "AND (:excludeId IS NULL OR a.id != :excludeId)")
    boolean existsOverlapping(
            @Param("supplierId") Long supplierId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("excludeId") Long excludeId,
            @Param("status") AvailabilityStatus status
    );
}