package com.timora.app.service.impl;

import com.timora.app.dto.security.CurrentUser;
import com.timora.app.dto.service.ServiceCreateDTO;
import com.timora.app.dto.service.ServiceDTO;
import com.timora.app.dto.service.ServicePatchDTO;
import com.timora.app.exception.BusinessException;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.exception.NotFoundException;
import com.timora.app.model.Company;
import com.timora.app.model.Service;
import com.timora.app.model.Supplier;
import com.timora.app.repository.CompanyRepository;
import com.timora.app.repository.ServiceRepository;
import com.timora.app.repository.SupplierRepository;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.ServiceService;
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
    private final CompanyRepository companyRepository;
    private final SupplierRepository supplierRepository;

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

        // Validar que exista la compañía
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Company not found"));

        // Validar que exista el supplier
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new NotFoundException("Supplier not found"));

        // Validar que supplier pertenezca a la compañía
        validateSameCompany(request.getCompanyId(), supplier.getCompany().getId(), "Supplier");

        // ========================================
        // 🔐 CONTROL DE ACCESO
        // ========================================
        if (!auth.isOwner(currentUser)) {
            if (auth.isAdmin(currentUser)) {
                if (!currentUser.getCompanyId().equals(request.getCompanyId())) {
                    throw new ForbiddenException("You are not allowed to create services in other companies");
                }
            } else {
                if (!currentUser.getPersonId().equals(supplier.getPerson().getId())) {
                    throw new ForbiddenException("You are not allowed to create services for other suppliers");
                }
            }
        }

        // Crear el servicio
        Service service = new Service();
        service.setCompany(company);
        service.setSupplier(supplier);
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setDuration(request.getDuration());
        service.setStatus(request.getStatus() != null ? request.getStatus() : com.timora.app.model.enums.ServiceStatus.ACTIVE);

        Service saved = serviceRepository.save(service);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public ServiceDTO patch(Long id, ServicePatchDTO request) {
        CurrentUser currentUser = securityHelper.getCurrentUser();
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service not found"));

        // ========================================
        // 🔐 CONTROL DE ACCESO
        // ========================================
        if (!auth.isOwner(currentUser)) {
            if (auth.isAdmin(currentUser)) {
                if (!currentUser.getCompanyId().equals(service.getCompany().getId())) {
                    throw new ForbiddenException("You are not allowed to modify services from other companies");
                }
            } else {
                if (!currentUser.getPersonId().equals(service.getSupplier().getPerson().getId())) {
                    throw new ForbiddenException("You are not allowed to modify this service");
                }
            }
        }

        // Actualizar supplier si se proporciona
        if (request.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new NotFoundException("Supplier not found"));

            validateSameCompany(service.getCompany().getId(), supplier.getCompany().getId(), "Supplier");

            if (!auth.isOwner(currentUser) && !auth.isAdmin(currentUser)) {
                if (!currentUser.getPersonId().equals(supplier.getPerson().getId())) {
                    throw new ForbiddenException("You can only assign services to yourself");
                }
            }

            service.setSupplier(supplier);
        }

        // Actualizar campos
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

        // ========================================
        // 🔐 CONTROL DE ACCESO
        // ========================================
        if (!auth.isOwner(currentUser)) {
            if (auth.isAdmin(currentUser)) {
                if (!currentUser.getCompanyId().equals(service.getCompany().getId())) {
                    throw new ForbiddenException("You are not allowed to delete services from other companies");
                }
            } else {
                if (!currentUser.getPersonId().equals(service.getSupplier().getPerson().getId())) {
                    throw new ForbiddenException("You are not allowed to delete this service");
                }
            }
        }

        // ✅ SOFT DELETE: Cambiar estado a INACTIVE
        service.setStatus(com.timora.app.model.enums.ServiceStatus.INACTIVE);
        serviceRepository.save(service);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceDTO> getAll() {
        CurrentUser currentUser = securityHelper.getCurrentUser();
        List<Service> services;

        // ========================================
        // 🔐 CONTROL DE ACCESO PARA LISTAR
        // ========================================
        if (auth.isOwner(currentUser)) {
            // Owner ve TODOS los servicios NO INACTIVE
            services = serviceRepository.findAllActive();
        } else if (auth.isAdmin(currentUser)) {
            // Admin ve solo los servicios NO INACTIVE de SU compañía
            services = serviceRepository.findByCompanyId(currentUser.getCompanyId());
        } else {
            // User ve solo SUS servicios NO INACTIVE (como supplier)
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

        // ========================================
        // 🔐 CONTROL DE ACCESO PARA VER POR ID
        // ========================================
        if (!auth.isOwner(currentUser)) {
            if (auth.isAdmin(currentUser)) {
                if (!currentUser.getCompanyId().equals(service.getCompany().getId())) {
                    throw new ForbiddenException("You are not allowed to view this service");
                }
            } else {
                if (!currentUser.getPersonId().equals(service.getSupplier().getPerson().getId())) {
                    throw new ForbiddenException("You are not allowed to view this service");
                }
            }
        }

        return toDTO(service);
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