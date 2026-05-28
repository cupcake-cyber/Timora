package com.timora.app.controller;

import com.timora.app.dto.*;
import com.timora.app.service.ServiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    // =========================
    // GET ALL (company scoped)
    // =========================
    @GetMapping
    public List<ServiceSummaryDTO> getAll() {
        return serviceService.findAll();
    }

    // =========================
    // GET BY ID
    // =========================
    @GetMapping("/{id}")
    public ServiceDetailsDTO getById(@PathVariable Long id) {
        return serviceService.getServiceById(id);
    }

    // =========================
    // GET BY SUPPLIER
    // =========================
    @GetMapping("/supplier/{supplierId}")
    public List<ServiceSummaryDTO> getBySupplier(@PathVariable Long supplierId) {
        return serviceService.getServicesBySupplier(supplierId);
    }

    // =========================
    // CREATE SERVICE
    // =========================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceDetailsDTO create(@Valid @RequestBody ServiceCreateDTO dto) {
        return serviceService.createService(dto);
    }

    // =========================
    // FULL UPDATE (optional admin use)
    // =========================
    @PutMapping("/{id}")
    public ServiceDetailsDTO update(
            @PathVariable Long id,
            @Valid @RequestBody ServiceUpdateDTO dto
    ) {
        return serviceService.updateService(id, dto);
    }

    // =========================
    // PARTIAL UPDATE (STATUS ONLY)
    // =========================
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        serviceService.updateStatus(id, status);
    }

    // =========================
    // DELETE (soft delete)
    // =========================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        serviceService.delete(id);
    }
}