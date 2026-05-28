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

    @GetMapping
    public List<ServiceSummaryDTO> getAllServices() {
        return serviceService.findAll();
    }

    @GetMapping("/supplier/{supplierId}")
    public List<ServiceSummaryDTO> getServicesBySupplier(@PathVariable Long supplierId) {
        return serviceService.getServicesBySupplier(supplierId);
    }

    @GetMapping("/{id}")
    public ServiceDetailsDTO getServiceById(@PathVariable Long id) {
        return serviceService.getServiceById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceDetailsDTO createService(@Valid @RequestBody ServiceCreateDTO dto) {
        return serviceService.createService(dto);
    }

    @PutMapping("/{id}")
    public ServiceDetailsDTO updateService(@PathVariable Long id,
                                           @Valid @RequestBody ServiceUpdateDTO dto) {
        return serviceService.updateService(id, dto);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Long id,
                             @RequestParam String status) {
        serviceService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteService(@PathVariable Long id) {
        serviceService.delete(id);
    }
}