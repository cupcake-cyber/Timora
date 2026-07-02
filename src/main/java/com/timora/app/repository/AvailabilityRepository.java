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

    // Métodos existentes
    List<Availability> findByCompanyIdAndStatus(Long companyId, AvailabilityStatus status);

    List<Availability> findBySupplierIdAndStatus(Long supplierId, AvailabilityStatus status);

    List<Availability> findBySupplierId(Long supplierId);

    List<Availability> findByCompanyId(Long companyId);

    @Query("SELECT a FROM Availability a WHERE a.supplier.id = :supplierId " +
            "AND a.status = :status " +
            "AND a.startDate <= :endDate " +
            "AND a.endDate >= :startDate")
    List<Availability> findOverlappingBySupplierAndDateRange(
            @Param("supplierId") Long supplierId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") AvailabilityStatus status
    );

    @Query("SELECT a FROM Availability a WHERE a.supplier.id = :supplierId " +
            "AND a.status = :status " +
            "AND a.startDate <= :date " +
            "AND a.endDate >= :date")
    List<Availability> findBySupplierIdAndDate(
            @Param("supplierId") Long supplierId,
            @Param("date") LocalDate date,
            @Param("status") AvailabilityStatus status
    );

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Availability a WHERE a.supplier.id = :supplierId " +
            "AND a.id != :availabilityId " +
            "AND a.status = :status " +
            "AND a.startDate <= :endDate " +
            "AND a.endDate >= :startDate")
    boolean existsOverlappingExcludingId(
            @Param("supplierId") Long supplierId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("availabilityId") Long availabilityId,
            @Param("status") AvailabilityStatus status
    );

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Availability a WHERE a.supplier.id = :supplierId " +
            "AND a.status = :status " +
            "AND a.startDate <= :endDate " +
            "AND a.endDate >= :startDate")
    boolean existsOverlapping(
            @Param("supplierId") Long supplierId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") AvailabilityStatus status
    );

    // Nuevo método para filtrar solo ACTIVE por company
    @Query("SELECT a FROM Availability a WHERE a.company.id = :companyId AND a.status = 'ACTIVE'")
    List<Availability> findActiveByCompanyId(@Param("companyId") Long companyId);

    // Nuevo método para filtrar solo ACTIVE por supplier
    @Query("SELECT a FROM Availability a WHERE a.supplier.id = :supplierId AND a.status = 'ACTIVE'")
    List<Availability> findActiveBySupplierId(@Param("supplierId") Long supplierId);

    // Método para verificar que el supplier pertenece a la compañía
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM Supplier s WHERE s.id = :supplierId AND s.company.id = :companyId")
    boolean existsSupplierInCompany(@Param("supplierId") Long supplierId, @Param("companyId") Long companyId);
}