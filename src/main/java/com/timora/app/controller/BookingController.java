package com.timora.app.controller;

import com.timora.app.dto.booking.BookingCreateDTO;
import com.timora.app.dto.booking.BookingDTO;
import com.timora.app.dto.booking.BookingPatchDTO;
import com.timora.app.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    /**
     * Crea una nueva reserva
     * POST /api/bookings
     */
    @PostMapping
    public ResponseEntity<BookingDTO> create(@RequestBody BookingCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.create(dto));
    }

    /**
     * Actualiza parcialmente una reserva
     * PATCH /api/bookings/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<BookingDTO> patch(
            @PathVariable Long id,
            @RequestBody BookingPatchDTO dto) {
        return ResponseEntity.ok(bookingService.patch(id, dto));
    }

    /**
     * Elimina (soft delete) una reserva
     * DELETE /api/bookings/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene todas las reservas de la compañía del usuario
     * GET /api/bookings
     */
    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllByCompany() {
        return ResponseEntity.ok(bookingService.getAllByCompany());
    }

    /**
     * Obtiene una reserva por ID
     * GET /api/bookings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }

    /**
     * Obtiene todas las reservas de un cliente
     * GET /api/bookings/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingDTO>> getAllByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(bookingService.getAllByCustomer(customerId));
    }

    /**
     * Obtiene todas las reservas de un servicio
     * GET /api/bookings/service/{serviceId}
     */
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<BookingDTO>> getAllByService(@PathVariable Long serviceId) {
        return ResponseEntity.ok(bookingService.getAllByService(serviceId));
    }

    /**
     * Obtiene todas las reservas de un proveedor (supplier)
     * GET /api/bookings/supplier/{supplierId}
     */
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<BookingDTO>> getAllBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(bookingService.getAllBySupplier(supplierId));
    }

    /**
     * Obtiene reservas de un proveedor en un rango de fechas
     * GET /api/bookings/supplier/{supplierId}/range?startDate=2026-01-01T00:00:00&endDate=2026-12-31T23:59:59
     */
    @GetMapping("/supplier/{supplierId}/range")
    public ResponseEntity<List<BookingDTO>> getBySupplierAndDateRange(
            @PathVariable Long supplierId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(
                bookingService.getBySupplierAndDateRange(supplierId, startDate, endDate)
        );
    }

    /**
     * Valida si hay solapamiento de reservas para un servicio en un rango de tiempo
     * GET /api/bookings/validate-overlap?serviceId=1&startTime=2026-07-15T10:00:00&endTime=2026-07-15T10:30:00&excludeId=5
     */
    @GetMapping("/validate-overlap")
    public ResponseEntity<Void> validateOverlap(
            @RequestParam Long serviceId,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            @RequestParam(required = false) Long excludeId) {
        bookingService.validateOverlap(serviceId, startTime, endTime, excludeId);
        return ResponseEntity.ok().build();
    }
}