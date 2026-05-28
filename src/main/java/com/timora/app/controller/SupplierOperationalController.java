package com.timora.app.controller;

import com.timora.app.dto.ProviderOperationalDTO;
import com.timora.app.model.Booking;
import com.timora.app.model.Service;
import com.timora.app.service.SupplierOperationalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierOperationalController {

    private final SupplierOperationalService supplierOperationalService;

    @GetMapping("/{id}/services")
    public ResponseEntity<List<Service>> getSupplierServices(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                supplierOperationalService.getSupplierServices(id)
        );
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<Booking>> getSupplierBookings(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                supplierOperationalService.getSupplierBookings(id)
        );
    }

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<ProviderOperationalDTO> getDashboard(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                supplierOperationalService.getDashboard(id)
        );
    }
}
