package com.timora.app.controller;

import com.timora.app.dto.AvailabilityCreateDTO;
import com.timora.app.dto.AvailabilityDTO;
import com.timora.app.service.AvailabilityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    // GET /api/availability
    @GetMapping
    public List<AvailabilityDTO> getAllAvailability() {
        return availabilityService.findAll();
    }

    // GET /api/availability/supplier/1
    @GetMapping("/supplier/{supplierId}")
    public List<AvailabilityDTO>
    getAvailabilityBySupplier(@PathVariable Long supplierId) {
        return availabilityService.getAvailabilityBySupplier(supplierId);
    }

    // POST /api/availability
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvailabilityDTO createAvailability(@Valid @RequestBody AvailabilityCreateDTO dto) {
        return availabilityService.createAvailability(dto);
    }

    // PATCH /api/availability/1/status?status=BLOCKED
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Long id, @RequestParam String status) {
        availabilityService.updateStatus(id, status);
    }

    // DELETE /api/availability/1
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAvailability(@PathVariable Long id) {
        availabilityService.delete(id);
    }
}
