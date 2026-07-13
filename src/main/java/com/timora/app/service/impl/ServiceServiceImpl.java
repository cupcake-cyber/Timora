package com.timora.app.service.impl;

import com.timora.app.dto.security.CurrentUser;
import com.timora.app.dto.service.ServiceCreateDTO;
import com.timora.app.dto.service.ServiceDTO;
import com.timora.app.dto.service.ServicePatchDTO;
import com.timora.app.exception.BusinessException;
import com.timora.app.exception.NotFoundException;
import com.timora.app.model.Company;
import com.timora.app.model.Person;
import com.timora.app.model.Service;
import com.timora.app.model.Supplier;
import com.timora.app.model.enums.ServiceStatus;
import com.timora.app.repository.ServiceRepository;
import com.timora.app.security.AccessControlBaseService;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.CompanyService;
import com.timora.app.service.PersonService;
import com.timora.app.service.ServiceService;
import com.timora.app.service.SupplierService;
import lombok.AllArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final SecurityHelper securityHelper;
    private final AccessControlService access;
    private final AccessControlBaseService accessBase;
    private final ServiceRepository serviceRepository;
    private final CompanyService companyService;
    private final SupplierService supplierService;
    private final PersonService personService;
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

        access.requireCanCreateService(currentUser, supplier);

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

        access.requireCanUpdateService(currentUser, service.getSupplier());

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

        access.requireCanDeleteService(currentUser, service.getSupplier());

        service.setStatus(ServiceStatus.INACTIVE);
        serviceRepository.save(service);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceDTO> getAll() {
        CurrentUser currentUser = securityHelper.getCurrentUser();
        List<Service> services;

        if (accessBase.isOwner(currentUser)) {
            services = serviceRepository.findAllActive();
        } else if (accessBase.isAdmin(currentUser)) {
            services = serviceRepository.findByCompanyId(currentUser.getCompanyId());
        } else {
            // USER: Debe poder ver servicios donde:
            // 1. Es supplier (sus propios servicios)
            // 2. Tiene permisos en user_supplier_permissions

            Set<Long> supplierIds = new HashSet<>();

            // 🔥 CASO 1: Si el usuario es supplier, agregar su propio supplierId
            try {
                Person currentPerson = personService.findById(currentUser.getPersonId());
                Supplier currentSupplier = currentPerson.getSupplier();
                if (currentSupplier != null) {
                    supplierIds.add(currentSupplier.getId());
                }
            } catch (Exception e) {
                // Si falla, continuar
            }

            // 🔥 CASO 2: Suppliers donde tiene permisos (user_supplier_permissions)
            List<Supplier> accessibleSuppliers = supplierService.findByUserId(currentUser.getUserId());
            if (!accessibleSuppliers.isEmpty()) {
                supplierIds.addAll(
                        accessibleSuppliers.stream()
                                .map(Supplier::getId)
                                .collect(Collectors.toSet())
                );
            }

            if (supplierIds.isEmpty()) {
                return List.of();
            }

            services = serviceRepository.findBySupplierIds(new ArrayList<>(supplierIds));
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

        access.requireCanReadService(currentUser, service.getSupplier());

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