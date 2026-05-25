package com.timora.app.service;

import com.timora.app.models.Service;
import com.timora.app.models.enums.ServiceStatus;

import java.util.List;
import java.util.Optional;

public interface ServiceService {

    List<Service> findAll();

    Optional<Service> findById(Long id);

    Service save(Service service);

    Service update(Long id, Service service);

    void delete(Long id);

    // Filtros
    List<Service> findByCompanyId(Long companyId);

    List<Service> findBySupplierId(Long supplierId);

    List<Service> findByStatus(ServiceStatus status);

    Optional<Service> findByName(String name);
}