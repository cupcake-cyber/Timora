package com.timora.app.controller;

import com.timora.app.dto.AvailabilityCreateDTO;
import com.timora.app.dto.AvailabilityDTO;
import com.timora.app.service.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @GetMapping
    public List<AvailabilityDTO> getMyAvailabilities() {
        return availabilityService.getMyAvailabilities();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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