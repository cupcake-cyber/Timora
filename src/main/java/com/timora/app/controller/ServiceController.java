package com.timora.app.controller;

import com.timora.app.model.Service;
import com.timora.app.model.enums.ServiceStatus;
import com.timora.app.service.ServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService service;

    public ServiceController(ServiceService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Service>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Service> getByName(@PathVariable String name) {
        return service.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Service>> getByStatus(@PathVariable ServiceStatus status) {
        return ResponseEntity.ok(service.findByStatus(status));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Service>> getByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(service.findByCompanyId(companyId));
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<Service>> getBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(service.findBySupplierId(supplierId));
    }

    @PostMapping
    public ResponseEntity<Service> create(@RequestBody Service serviceBody) {
        Service created = service.save(serviceBody);
        return ResponseEntity.created(URI.create("/api/services/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> update(@PathVariable Long id, @RequestBody Service serviceBody) {
        return ResponseEntity.ok(service.update(id, serviceBody));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
