package com.timora.app.repository;

import com.timora.app.model.Availability;
import com.timora.app.model.enums.AvailabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByCompanyId(Long companyId);
    List<Availability> findByCompanyIdAndSupplierId(Long companyId, Long supplierId);
    Optional<Availability> findByIdAndCompanyId(Long id, Long companyId);
    Optional<Availability> findByIdAndCompanyIdAndSupplierId(Long id, Long companyId, Long supplierId);
    List<Availability> findByCompanyIdAndSupplierIdAndStatus(Long companyId, Long supplierId, AvailabilityStatus status);
    List<Availability> findByCompanyIdAndSupplierIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long companyId, Long supplierId, AvailabilityStatus status, LocalDate currentDate, LocalDate currentDate2);
    List<Availability> findByCompanyIdAndSupplierIdAndDayOfWeekAndStatus(
            Long companyId, Long supplierId, DayOfWeek dayOfWeek, AvailabilityStatus status);

    @Query("""
    SELECT a
    FROM Availability a
    WHERE a.company.id = :companyId
      AND a.supplier.id = :supplierId
      AND a.status <> 'INACTIVE'
      AND (
            a.startDate <= :endDate
            AND a.endDate >= :startDate
      )
      AND (
            a.dayOfWeek = :dayOfWeek
            OR a.dayOfWeek IS NULL
      )
      AND (
            a.startTime < :endTime
            AND a.endTime > :startTime
      )
""")
    List<Availability> findOverlappingAvailabilities(
            @Param("companyId") Long companyId,
            @Param("supplierId") Long supplierId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
