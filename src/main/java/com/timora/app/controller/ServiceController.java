package com.timora.app.controller;

import com.timora.app.dto.service.ServiceCreateDTO;
import com.timora.app.dto.service.ServiceDTO;
import com.timora.app.dto.service.ServicePatchDTO;
import com.timora.app.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceManagementService;

    @PostMapping
    public ResponseEntity<ServiceDTO> create(@RequestBody ServiceCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceManagementService.create(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ServiceDTO> patch(@PathVariable Long id, @RequestBody ServicePatchDTO dto) {
        return ResponseEntity.ok(serviceManagementService.patch(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        serviceManagementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAll() {
        return ResponseEntity.ok(serviceManagementService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceManagementService.getById(id));
    }
}