package com.timora.app.controller;

import com.timora.app.model.Availability;
import com.timora.app.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
@RequiredArgsConstructor
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    @GetMapping
    public List<Availability> getAll() {
        return availabilityService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Availability> getById(@PathVariable Long id) {

        return availabilityService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Availability create(@RequestBody Availability availability) {
        return availabilityService.save(availability);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Availability> update(
            @PathVariable Long id,
            @RequestBody Availability availability
    ) {

        return ResponseEntity.ok(
                availabilityService.update(id, availability)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        availabilityService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
