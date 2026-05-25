package com.timora.app.service.impl;

import com.timora.app.model.Service;
import com.timora.app.model.enums.ServiceStatus;
import com.timora.app.repository.ServiceRepository;
import com.timora.app.service.ServiceService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Service> findById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    public Service save(Service service) {

        service.setId(null);
        service.setCreatedAt(LocalDateTime.now());

        // 🔥 CORRECTO (enum en inglés)
        if (service.getStatus() == null) {
            service.setStatus(ServiceStatus.ACTIVE);
        }

        return serviceRepository.save(service);
    }

    @Override
    public Service update(Long id, Service updated) {
        return serviceRepository.findById(id).map(service -> {

            // Relaciones
            service.setCompany(updated.getCompany());
            service.setSupplier(updated.getSupplier());

            // Campos normales
            service.setName(updated.getName());
            service.setDescription(updated.getDescription());
            service.setPrice(updated.getPrice());
            service.setDuration(updated.getDuration());
            service.setStatus(updated.getStatus());

            return serviceRepository.save(service);

        }).orElseThrow(() -> new RuntimeException("Service no encontrado con id: " + id));
    }

    @Override
    public void delete(Long id) {
        serviceRepository.deleteById(id);
    }

    @Override
    public List<Service> findByCompanyId(Long companyId) {
        return serviceRepository.findByCompanyId(companyId);
    }

    @Override
    public List<Service> findBySupplierId(Long supplierId) {
        return serviceRepository.findBySupplierId(supplierId);
    }

    @Override
    public List<Service> findByStatus(ServiceStatus status) {
        return serviceRepository.findByStatus(status);
    }

    @Override
    public Optional<Service> findByName(String name) {
        return serviceRepository.findByName(name);
    }
}