package com.timora.app.controller;

import com.timora.app.model.Booking;
import com.timora.app.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // 🔹 Crear booking
    @PostMapping
    public ResponseEntity<Booking> create(@RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.create(booking));
    }

    // 🔹 Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }

    // 🔹 Por empresa
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Booking>> getByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(bookingService.getByCompany(companyId));
    }

    // 🔹 Por cliente
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Booking>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(bookingService.getByCustomer(customerId));
    }

    // 🔹 Por supplier
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<Booking>> getBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(bookingService.getBySupplier(supplierId));
    }

    // 🔹 Por estado
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Booking>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(bookingService.getByStatus(status));
    }

    // 🔹 Por rango de fechas
    @GetMapping("/between")
    public ResponseEntity<List<Booking>> getBetweenDates(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(bookingService.getBetweenDates(start, end));
    }

    // 🔹 Confirmar
    @PutMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.confirm(id));
    }

    // 🔹 Cancelar
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancel(id));
    }

    // 🔹 Completar
    @PutMapping("/{id}/complete")
    public ResponseEntity<Booking> complete(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.complete(id));
    }

    // 🔹 Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}