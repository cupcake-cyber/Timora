package com.timora.app.service.impl;

import com.timora.app.dto.security.CurrentUser;
import com.timora.app.dto.service.ServiceCreateDTO;
import com.timora.app.dto.service.ServiceDTO;
import com.timora.app.dto.service.ServicePatchDTO;
import com.timora.app.exception.BusinessException;
import com.timora.app.exception.NotFoundException;
import com.timora.app.model.Company;
import com.timora.app.model.Service;
import com.timora.app.model.Supplier;
import com.timora.app.model.enums.ServiceStatus;
import com.timora.app.repository.ServiceRepository;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.CompanyService;
import com.timora.app.service.ServiceService;
import com.timora.app.service.SupplierService;
import lombok.AllArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final SecurityHelper securityHelper;
    private final AccessControlService auth;
    private final ServiceRepository serviceRepository;
    private final CompanyService companyService;
    private final SupplierService supplierService;

    private void validateSameCompany(Long baseCompanyId, Long entityCompanyId, String entityName) {
        if (entityCompanyId == null) return;
        if (!Objects.equals(baseCompanyId, entityCompanyId)) {
            throw new BusinessException(entityName + " must belong to the same company");
        }
    }

    @Override
    @Transactional
    public ServiceDTO create(ServiceCreateDTO request) {
        CurrentUser currentUser = securityHelper.getCurrentUser();

        Company company = companyService.getByIdEntity(request.getCompanyId());
        Supplier supplier = supplierService.findById(request.getSupplierId());

        validateSameCompany(request.getCompanyId(), supplier.getCompany().getId(), "Supplier");

        auth.requireCanCreateService(currentUser, supplier);

        Service service = new Service();
        service.setCompany(company);
        service.setSupplier(supplier);
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setDuration(request.getDuration());
        service.setStatus(request.getStatus() != null ? request.getStatus() : ServiceStatus.ACTIVE);

        Service saved = serviceRepository.save(service);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public ServiceDTO patch(Long id, ServicePatchDTO request) {
        CurrentUser currentUser = securityHelper.getCurrentUser();
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service not found"));

        auth.requireCanUpdateService(currentUser, service.getSupplier());

        if (request.getSupplierId() != null) {
            Supplier supplier = supplierService.findById(request.getSupplierId());
            validateSameCompany(service.getCompany().getId(), supplier.getCompany().getId(), "Supplier");
            service.setSupplier(supplier);
        }

        if (request.getName() != null) {
            service.setName(request.getName());
        }
        if (request.getDescription() != null) {
            service.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            service.setPrice(request.getPrice());
        }
        if (request.getDuration() != null) {
            service.setDuration(request.getDuration());
        }
        if (request.getStatus() != null) {
            service.setStatus(request.getStatus());
        }

        Service updated = serviceRepository.save(service);
        return toDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CurrentUser currentUser = securityHelper.getCurrentUser();
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service not found"));

        auth.requireCanDeleteService(currentUser, service.getSupplier());

        service.setStatus(ServiceStatus.INACTIVE);
        serviceRepository.save(service);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceDTO> getAll() {
        CurrentUser currentUser = securityHelper.getCurrentUser();
        List<Service> services;

        if (auth.isOwner(currentUser)) {
            services = serviceRepository.findAllActive();
        } else if (auth.isAdmin(currentUser)) {
            services = serviceRepository.findByCompanyId(currentUser.getCompanyId());
        } else {
            services = serviceRepository.findBySupplierPersonId(currentUser.getPersonId());
        }

        return services.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceDTO getById(Long id) {
        CurrentUser currentUser = securityHelper.getCurrentUser();
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service not found"));

        auth.requireCanReadService(currentUser, service.getSupplier());

        return toDTO(service);
    }

    @Override
    public Service getByIdEntity(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service not found with id: " + id));
    }

    private ServiceDTO toDTO(Service service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setId(service.getId());
        dto.setCompanyId(service.getCompany().getId());
        dto.setCompanyName(service.getCompany().getName());
        dto.setSupplierId(service.getSupplier().getId());
        dto.setSupplierName(service.getSupplier().getPerson().getFirstName() + " " +
                service.getSupplier().getPerson().getLastName());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setPrice(service.getPrice());
        dto.setDuration(service.getDuration());
        dto.setStatus(service.getStatus());
        dto.setCreatedAt(service.getCreatedAt());
        return dto;
    }
}