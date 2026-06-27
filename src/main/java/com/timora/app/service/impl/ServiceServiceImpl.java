//package com.timora.app.service.impl;
//
//import com.timora.app.dto.*;
//import com.timora.app.exception.BusinessException;
//import com.timora.app.exception.ForbiddenException;
//import com.timora.app.exception.NotFoundException;
//import com.timora.app.model.Service;
//import com.timora.app.model.Supplier;
//import com.timora.app.model.User;
//import com.timora.app.model.enums.ServiceStatus;
//import com.timora.app.repository.ServiceRepository;
//import com.timora.app.repository.SupplierRepository;
//import com.timora.app.security.AccessControlService;
//import com.timora.app.security.SecurityHelper;
//import com.timora.app.service.ServiceService;
//import lombok.AllArgsConstructor;
//
//import java.util.List;
//
//@org.springframework.stereotype.Service
//@AllArgsConstructor
//public class ServiceServiceImpl implements ServiceService {
//
//    private final ServiceRepository serviceRepository;
//    private final SupplierRepository supplierRepository;
//    private final AccessControlService auth;
//    private final SecurityHelper securityHelper;
//
//    // =========================
//    // GET ALL
//    // =========================
//    @Override
//    public List<ServiceSummaryDTO> findAll() {
//
//        User user = securityHelper.getCurrentUser();
//        Long companyId = user.getCompany().getId();
//
//        if (auth.isOwner(user)) {
//            return serviceRepository.findAllSummaryGlobal();
//        }
//
//        if (auth.isAdmin(user)) {
//            return serviceRepository.findAllSummary(companyId);
//        }
//
//        Supplier supplier = getUserSupplier(user);
//        return serviceRepository.findSummaryBySupplier(companyId, supplier.getId());
//    }
//
//    // =========================
//    // GET BY SUPPLIER
//    // =========================
//    @Override
//    public List<ServiceSummaryDTO> getServicesBySupplier(Long supplierId) {
//
//        User user = securityHelper.getCurrentUser();
//        Long companyId = user.getCompany().getId();
//
//        Supplier supplier = supplierRepository.findByIdAndCompanyId(supplierId, companyId)
//                .orElseThrow(() -> new NotFoundException("Supplier not found"));
//
//        if (!auth.isOwner(user)) {
//
//            if (!serviceRepository.existsByCompanyIdAndSupplierIdAndNameIgnoreCase(
//                    companyId, supplier.getId(), supplier.getNotes()
//            )) {
//                auth.requireSameCompany(user, companyId);
//            }
//
//            if (auth.isUser(user)) {
//                Supplier mySupplier = getUserSupplier(user);
//
//                if (!mySupplier.getId().equals(supplier.getId())) {
//                    throw new ForbiddenException("USER can only access own supplier services");
//                }
//            }
//        }
//
//        return serviceRepository.findSummaryBySupplier(companyId, supplier.getId());
//    }
//
//    // =========================
//    // GET BY ID
//    // =========================
//    @Override
//    public ServiceDetailsDTO getServiceById(Long id) {
//
//        User user = securityHelper.getCurrentUser();
//
//        Service service = serviceRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Service not found"));
//
//        Long companyId = user.getCompany().getId();
//
//        if (!auth.isOwner(user)) {
//
//            if (!service.getCompany().getId().equals(companyId)) {
//                throw new ForbiddenException("Different company");
//            }
//
//            if (auth.isUser(user)) {
//
//                Supplier mySupplier = getUserSupplier(user);
//
//                if (!service.getSupplier().getId().equals(mySupplier.getId())) {
//                    throw new ForbiddenException("USER can only view own services");
//                }
//            }
//        }
//
//        return serviceRepository.findDetails(id, companyId)
//                .orElseThrow(() -> new NotFoundException("Service not found"));
//    }
//
//    // =========================
//    // CREATE (USER PUEDE)
//    // =========================
//    @Override
//    public ServiceDetailsDTO createService(ServiceCreateDTO dto) {
//
//        User user = securityHelper.getCurrentUser();
//        Long companyId = user.getCompany().getId();
//
//        Supplier supplier;
//
//        if (auth.isUser(user)) {
//            supplier = getUserSupplier(user);
//        } else {
//            supplier = supplierRepository.findByIdAndCompanyId(dto.getSupplierId(), companyId)
//                    .orElseThrow(() -> new NotFoundException("Supplier not found"));
//        }
//
//        boolean exists = serviceRepository
//                .existsByCompanyIdAndSupplierIdAndNameIgnoreCase(
//                        companyId,
//                        supplier.getId(),
//                        dto.getName()
//                );
//
//        if (exists) {
//            throw new BusinessException("Service already exists");
//        }
//
//        Service service = new Service();
//        service.setCompany(user.getCompany());
//        service.setSupplier(supplier);
//        service.setName(dto.getName());
//        service.setDescription(dto.getDescription());
//        service.setPrice(dto.getPrice());
//        service.setDuration(dto.getDuration());
//        service.setStatus(ServiceStatus.ACTIVE);
//
//        serviceRepository.save(service);
//
//        return serviceRepository.findDetails(service.getId(), companyId)
//                .orElseThrow(() -> new NotFoundException("Service not found"));
//    }
//
//    // =========================
//    // UPDATE (USER SOLO SUYOS)
//    // =========================
//    @Override
//    public ServiceDetailsDTO updateService(Long id, ServiceUpdateDTO dto) {
//
//        User user = securityHelper.getCurrentUser();
//
//        Service service = serviceRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Service not found"));
//
//        if (!auth.isOwner(user)) {
//
//            if (!service.getCompany().getId().equals(user.getCompany().getId())) {
//                throw new ForbiddenException("Different company");
//            }
//
//            if (auth.isUser(user)) {
//                Supplier mySupplier = getUserSupplier(user);
//
//                if (!service.getSupplier().getId().equals(mySupplier.getId())) {
//                    throw new ForbiddenException("USER can only update own services");
//                }
//            }
//        }
//
//        service.setName(dto.getName());
//        service.setDescription(dto.getDescription());
//        service.setPrice(dto.getPrice());
//        service.setDuration(dto.getDuration());
//
//        serviceRepository.save(service);
//
//        return serviceRepository.findDetails(id, service.getCompany().getId())
//                .orElseThrow(() -> new NotFoundException("Service not found"));
//    }
//
//    // =========================
//    // STATUS
//    // =========================
//    @Override
//    public void updateStatus(Long id, String status) {
//
//        User user = securityHelper.getCurrentUser();
//
//        Service service = serviceRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Service not found"));
//
//        if (!auth.isOwner(user)) {
//
//            if (!service.getCompany().getId().equals(user.getCompany().getId())) {
//                throw new ForbiddenException("Different company");
//            }
//
//            if (auth.isUser(user)) {
//                Supplier mySupplier = getUserSupplier(user);
//
//                if (!service.getSupplier().getId().equals(mySupplier.getId())) {
//                    throw new ForbiddenException("USER cannot change other supplier services");
//                }
//            }
//        }
//
//        service.setStatus(ServiceStatus.valueOf(status.toUpperCase()));
//        serviceRepository.save(service);
//    }
//
//    // =========================
//    // DELETE (USER PUEDE BORRAR LOS SUYOS)
//    // =========================
//    @Override
//    public void delete(Long id) {
//
//        User user = securityHelper.getCurrentUser();
//
//        Service service = serviceRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Service not found"));
//
//        if (!auth.isOwner(user)) {
//
//            if (!service.getCompany().getId().equals(user.getCompany().getId())) {
//                throw new ForbiddenException("Different company");
//            }
//
//            if (auth.isUser(user)) {
//                Supplier mySupplier = getUserSupplier(user);
//
//                if (!service.getSupplier().getId().equals(mySupplier.getId())) {
//                    throw new ForbiddenException("USER can only delete own services");
//                }
//            }
//        }
//
//        service.setStatus(ServiceStatus.INACTIVE);
//        serviceRepository.save(service);
//    }
//
//    // =========================
//    // HELPERS
//    // =========================
//    private Supplier getUserSupplier(User user) {
//        return supplierRepository.findByPersonId(user.getPerson().getId())
//                .orElseThrow(() -> new NotFoundException("USER not linked to supplier"));
//    }
//}