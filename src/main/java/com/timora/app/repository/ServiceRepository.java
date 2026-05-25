package com.timora.app.repository;

import com.timora.app.model.Service;
import com.timora.app.model.enums.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    Optional<Service> findByName(String name);

    List<Service> findByStatus(ServiceStatus status);

    List<Service> findBySupplierId(Long supplierId);

    List<Service> findByCompanyId(Long companyId);
}