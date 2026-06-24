package com.timora.app.controller;

import com.timora.app.dto.AvailabilityCreateDTO;
import com.timora.app.dto.AvailabilityDTO;
import com.timora.app.service.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/availabilities")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @GetMapping
    public ResponseEntity<List<AvailabilityDTO>> getAll() {
        return ResponseEntity.ok(availabilityService.getMyAvailabilities());
    }

    @GetMapping
    public ResponseEntity<AvailabilityDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(availabilityService.);
    }

    @PostMapping
    public AvailabilityDTO create(@Valid @RequestBody AvailabilityCreateDTO dto) {
        return availabilityService.createAvailability(dto);
    }

    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable Long id, @RequestParam String status) {
        availabilityService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        availabilityService.delete(id);
    }
}