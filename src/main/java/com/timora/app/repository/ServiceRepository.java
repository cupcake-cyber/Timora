package com.timora.app.repository;

import com.timora.app.dto.ServiceSummaryDTO;
import com.timora.app.dto.ServiceDetailsDTO;
import com.timora.app.model.Service;
import com.timora.app.model.enums.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    // =========================
    // ENTITY QUERIES (BASE)
    // =========================

    List<Service> findByCompanyId(Long companyId);

    List<Service> findByCompanyIdAndSupplierId(Long companyId, Long supplierId);

    Optional<Service> findByIdAndCompanyId(Long id, Long companyId);

    Optional<Service> findByIdAndCompanyIdAndSupplierId(Long id, Long companyId, Long supplierId);

    List<Service> findByCompanyIdAndStatus(Long companyId, ServiceStatus status);

    List<Service> findByCompanyIdAndSupplierIdAndStatus(Long companyId, Long supplierId, ServiceStatus status);

    boolean existsByCompanyIdAndSupplierIdAndNameIgnoreCase(
            Long companyId,
            Long supplierId,
            String name
    );

    // =========================
    // DTO: SUMMARY (SAFE)
    // =========================

    @Query("""
        SELECT new com.timora.app.dto.ServiceSummaryDTO(
            s.id,
            s.name,
            s.price,
            s.duration,
            s.status,
            s.supplier.id,
            p.firstName
        )
        FROM Service s
        JOIN s.supplier sp
        JOIN sp.person p
        WHERE s.company.id = :companyId
    """)
    List<ServiceSummaryDTO> findAllSummary(@Param("companyId") Long companyId);

    @Query("""
        SELECT new com.timora.app.dto.ServiceSummaryDTO(
            s.id,
            s.name,
            s.price,
            s.duration,
            s.status,
            s.supplier.id,
            p.firstName
        )
        FROM Service s
        JOIN s.supplier sp
        JOIN sp.person p
        WHERE s.company.id = :companyId
        AND s.supplier.id = :supplierId
    """)
    List<ServiceSummaryDTO> findSummaryBySupplier(
            @Param("companyId") Long companyId,
            @Param("supplierId") Long supplierId
    );

    // =========================
    // DTO: DETAILS (SAFE)
    // =========================

    @Query("""
    SELECT new com.timora.app.dto.ServiceSummaryDTO(
        s.id,
        s.name,
        s.price,
        s.duration,
        s.status,
        sp.id,
        p.firstName
    )
    FROM Service s
    JOIN s.supplier sp
    JOIN sp.person p
    WHERE s.company.id = :companyId
    AND s.supplier.id = :supplierId
""")
    Optional<ServiceDetailsDTO> findDetails(
            @Param("id") Long id,
            @Param("companyId") Long companyId
    );
}