package com.timora.app.service.impl;

import com.timora.app.dto.*;
import com.timora.app.model.Supplier;
import com.timora.app.model.User;
import com.timora.app.model.enums.ServiceStatus;
import com.timora.app.repository.ServiceRepository;
import com.timora.app.repository.SupplierRepository;
import com.timora.app.repository.UserRepository;
import com.timora.app.service.ServiceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    public ServiceServiceImpl(
            ServiceRepository serviceRepository,
            SupplierRepository supplierRepository,
            UserRepository userRepository
    ) {
        this.serviceRepository = serviceRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ServiceSummaryDTO> findAll() {
        Long companyId = getCurrentUser().getCompany().getId();
        return serviceRepository.findAllSummary(companyId);
    }

    @Override
    public List<ServiceSummaryDTO> getServicesBySupplier(Long supplierId) {
        Long companyId = getCurrentUser().getCompany().getId();
        validateSupplierOwnership(supplierId, companyId);

        return serviceRepository.findSummaryBySupplier(companyId, supplierId);
    }

    @Override
    public ServiceDetailsDTO getServiceById(Long id) {
        Long companyId = getCurrentUser().getCompany().getId();

        return serviceRepository.findDetails(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado"));
    }

    @Override
    public ServiceDetailsDTO createService(ServiceCreateDTO dto) {

        User currentUser = getCurrentUser();
        Long companyId = currentUser.getCompany().getId();

        Supplier supplier = validateSupplierOwnership(dto.getSupplierId(), companyId);

        boolean exists = serviceRepository
                .existsByCompanyIdAndSupplierIdAndNameIgnoreCase(
                        companyId,
                        dto.getSupplierId(),
                        dto.getName()
                );

        if (exists) {
            throw new IllegalArgumentException("Ya existe un servicio con ese nombre.");
        }

        com.timora.app.model.Service service = new com.timora.app.model.Service();
        service.setCompany(currentUser.getCompany());
        service.setSupplier(supplier);
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        service.setDuration(dto.getDuration());
        service.setStatus(ServiceStatus.ACTIVE);

        serviceRepository.save(service);

        return serviceRepository.findDetails(service.getId(), companyId)
                .orElseThrow();
    }

    @Override
    public ServiceDetailsDTO updateService(Long id, ServiceUpdateDTO dto) {

        Long companyId = getCurrentUser().getCompany().getId();

        com.timora.app.model.Service service =
                serviceRepository.findByIdAndCompanyId(id, companyId)
                        .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado"));

        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        service.setDuration(dto.getDuration());

        serviceRepository.save(service);

        return serviceRepository.findDetails(id, companyId)
                .orElseThrow();
    }

    @Override
    public void updateStatus(Long id, String status) {

        Long companyId = getCurrentUser().getCompany().getId();

        com.timora.app.model.Service service =
                serviceRepository.findByIdAndCompanyId(id, companyId)
                        .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado"));

        service.setStatus(ServiceStatus.valueOf(status.toUpperCase()));
        serviceRepository.save(service);
    }

    @Override
    public void delete(Long id) {

        Long companyId = getCurrentUser().getCompany().getId();

        com.timora.app.model.Service service =
                serviceRepository.findByIdAndCompanyId(id, companyId)
                        .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado"));

        service.setStatus(ServiceStatus.ARCHIVED);
        serviceRepository.save(service);
    }

    private Supplier validateSupplierOwnership(Long supplierId, Long companyId) {
        return supplierRepository.findByIdAndCompanyId(supplierId, companyId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Proveedor no encontrado en la empresa actual"));
    }

    private User getCurrentUser() {
        String email = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByLoginEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }
}