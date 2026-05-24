package com.timora.app.repository;

import com.timora.app.models.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    // Buscar por nombre
    Optional<Service> findByName(String name);

    // Buscar por estado
    List<Service> findByStatus(ServiceStatus status);

    // Buscar por supplier
    List<Service> findBySupplierId(Long supplierId);

    // Buscar por company
    List<Service> findByCompanyId(Long companyId);
}
