package com.timora.app.repository;

import com.timora.app.models.Service;
import com.timora.app.models.enums.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    // Buscar por nombre
    Optional<Service> findByName(String name);

    // Buscar por estado
    List<Service> findByStatus(ServiceStatus status);

    // Buscar por supplier (relación)
    List<Service> findBySupplierId(Long supplierId);

    // Buscar por company (relación)
    List<Service> findByCompanyId(Long companyId);
}