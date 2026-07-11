package com.timora.app.controller;

import com.timora.app.dto.availability.AvailabilityCreateDTO;
import com.timora.app.dto.availability.AvailabilityDTO;
import com.timora.app.dto.availability.AvailabilityPatchDTO;
import com.timora.app.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/availabilities")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping
    public ResponseEntity<AvailabilityDTO> create(@RequestBody AvailabilityCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(availabilityService.create(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AvailabilityDTO> patch(
            @PathVariable Long id,
            @RequestBody AvailabilityPatchDTO dto) {
        return ResponseEntity.ok(availabilityService.patch(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        availabilityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AvailabilityDTO>> getAllByCompany() {
        return ResponseEntity.ok(availabilityService.getAllByCompany());
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<AvailabilityDTO>> getAllBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(availabilityService.getAllBySupplier(supplierId));
    }

    @GetMapping("/supplier/{supplierId}/date")
    public ResponseEntity<List<AvailabilityDTO>> getBySupplierAndDate(
            @PathVariable Long supplierId,
            @RequestParam LocalDate date) {
        return ResponseEntity.ok(availabilityService.getBySupplierAndDate(supplierId, date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailabilityDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(availabilityService.getById(id));
    }

//    @GetMapping("/validate-overlap")
//    public ResponseEntity<Void> validateOverlap(
//            @RequestParam Long supplierId,
//            @RequestParam LocalDate startDate,
//            @RequestParam(required = false) LocalDate endDate,  // ← Opcional
//            @RequestParam(required = false) Long excludeId) {   // ← Opcional
//        availabilityService.validateOverlap(supplierId, startDate, endDate, excludeId);
//        return ResponseEntity.ok().build();
//    }
}