package com.timora.app.controller;

import com.timora.app.dto.BookingDetailsDTO;
import com.timora.app.dto.BookingSummaryDTO;
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

    // =========================
    // CREATE (ENTITY IN)
    // =========================
    @PostMapping
    public ResponseEntity<Booking> create(@RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.create(booking));
    }

    // =========================
    // READ (DTO OUT)
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<BookingDetailsDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<BookingSummaryDTO>> getByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(bookingService.getByCompany(companyId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingSummaryDTO>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(bookingService.getByCustomer(customerId));
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<BookingSummaryDTO>> getBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(bookingService.getBySupplier(supplierId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookingSummaryDTO>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(bookingService.getByStatus(status));
    }

    @GetMapping("/between")
    public ResponseEntity<List<BookingSummaryDTO>> getBetweenDates(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(bookingService.getBetweenDates(start, end));
    }

    // =========================
    // STATE CHANGES (ENTITY OUT)
    // =========================
    @PutMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.confirm(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancel(id));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Booking> complete(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.complete(id));
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}