package com.timora.app.controller;

import com.timora.app.dto.ServiceCreateDTO;
import com.timora.app.dto.ServiceDetailsDTO;
import com.timora.app.dto.ServiceSummaryDTO;
import com.timora.app.dto.ServiceUpdateDTO;
import com.timora.app.model.Service;
import com.timora.app.model.enums.ServiceStatus;
import com.timora.app.service.ServiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController( ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    // GET /api/services
    @GetMapping
    public List<ServiceSummaryDTO> getAllServices() {
        return serviceService.findAll();
    }

    // GET /api/services/supplier/1
    @GetMapping("/supplier/{supplierId}")
    public List<ServiceSummaryDTO> getServicesBySupplier(@PathVariable Long supplierId) {
    return serviceService.getServicesBySupplier(supplierId);
    }

    // GET /api/services/1
    @GetMapping("/{id}")
    public ServiceDetailsDTO getServiceById(@PathVariable Long id) {
        return serviceService.getServiceById(id);
    }

    // POST /api/services
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceDetailsDTO createService(@Valid @RequestBody ServiceCreateDTO dto) {
        return serviceService.createService(dto);
    }

    // PUT /api/services/1
    @PutMapping("/{id}")
    public ServiceDetailsDTO updateService(@PathVariable Long id, @Valid @RequestBody ServiceUpdateDTO dto) {
        return serviceService.updateService(id, dto);
    }

    // PATCH /api/services/1/status?status=ACTIVE
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Long id, @RequestParam String status) {
        serviceService.updateStatus(id, status);
    }

    // DELETE /api/services/1
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteService(@PathVariable Long id) {
        serviceService.delete(id);
    }
}
