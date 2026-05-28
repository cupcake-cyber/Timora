package com.timora.app.repository;

import com.timora.app.model.Service;
import com.timora.app.model.enums.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findByCompanyId(Long companyId);
    List<Service> findByCompanyIdAndSupplierId(Long companyId, Long supplierId);
    Optional<Service> findByIdAndCompanyId(Long id, Long companyId);
    Optional<Service> findByIdAndCompanyIdAndSupplierId(Long id, Long companyId, Long supplierId);
    List<Service> findByCompanyIdAndStatus(Long companyId, ServiceStatus status);
    List<Service> findByCompanyIdAndSupplierIdAndStatus(Long companyId, Long supplierId, ServiceStatus status);
    boolean existsByCompanyIdAndSupplierIdAndNameIgnoreCase(Long companyId, Long supplierId, String name);
}