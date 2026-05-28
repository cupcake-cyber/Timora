package com.timora.app.repository;

import com.timora.app.dto.ServiceDetailsDTO;
import com.timora.app.dto.ServiceSummaryDTO;
import com.timora.app.model.enums.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ServiceRepository extends JpaRepository<com.timora.app.model.Service, Long> {

    // =========================
    // BASIC LOOKUPS (SIN INACTIVE)
    // =========================

    List<com.timora.app.model.Service> findByCompanyIdAndStatusNot(
            Long companyId,
            ServiceStatus status
    );

    Optional<com.timora.app.model.Service> findByIdAndStatusNot(
            Long id,
            ServiceStatus status
    );

    Optional<com.timora.app.model.Service> findByIdAndCompanyIdAndStatusNot(
            Long id,
            Long companyId,
            ServiceStatus status
    );

    List<com.timora.app.model.Service> findByCompanyIdAndSupplierIdAndStatusNot(
            Long companyId,
            Long supplierId,
            ServiceStatus status
    );

    boolean existsByCompanyIdAndSupplierIdAndNameIgnoreCase(
            Long companyId,
            Long supplierId,
            String name
    );
    List<com.timora.app.model.Service> findBySupplierId(Long supplierId);
    // =========================
    // SUMMARY (ADMIN / COMPANY)
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
        AND s.status <> com.timora.app.model.enums.ServiceStatus.INACTIVE
    """)
    List<ServiceSummaryDTO> findAllSummary(@Param("companyId") Long companyId);

    // =========================
    // SUMMARY BY SUPPLIER
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
        AND s.status <> com.timora.app.model.enums.ServiceStatus.INACTIVE
    """)
    List<ServiceSummaryDTO> findSummaryBySupplier(
            @Param("companyId") Long companyId,
            @Param("supplierId") Long supplierId
    );

    // =========================
    // DETAILS (BY ID + ACTIVE ONLY)
    // =========================

    @Query("""
        SELECT new com.timora.app.dto.ServiceDetailsDTO(
            s.id,
            s.company.id,
            s.supplier.id,
            p.firstName,
            s.name,
            s.description,
            s.price,
            s.duration,
            s.status,
            s.createdAt
        )
        FROM Service s
        JOIN s.supplier sp
        JOIN sp.person p
        WHERE s.id = :id
        AND s.company.id = :companyId
        AND s.status <> com.timora.app.model.enums.ServiceStatus.INACTIVE
    """)
    Optional<ServiceDetailsDTO> findDetails(
            @Param("id") Long id,
            @Param("companyId") Long companyId
    );

    // =========================
    // GLOBAL (OWNER)
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
        WHERE s.status <> com.timora.app.model.enums.ServiceStatus.INACTIVE
    """)
    List<ServiceSummaryDTO> findAllSummaryGlobal();
}