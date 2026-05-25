package com.timora.app.controller;

import com.timora.app.model.Booking;
import com.timora.app.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @GetMapping
    public ResponseEntity<List<Booking>> findAll() {
        return ResponseEntity.ok(bookingService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Booking> findById(@PathVariable Long id) {
        return bookingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Booking> create(@RequestBody Booking booking) {
        Booking saved = bookingService.save(booking);
        return ResponseEntity.ok(saved);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Booking> update(@PathVariable Long id, @RequestBody Booking booking) {
        try {
            Booking updated = bookingService.update(id, booking);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Booking>> findByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(bookingService.findByCustomerId(customerId));
    }


    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<Booking>> findByService(@PathVariable Long serviceId) {
        return ResponseEntity.ok(bookingService.findByServiceId(serviceId));
    }


    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Booking>> findByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(bookingService.findByCompanyId(companyId));
    }


    @PutMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirm(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookingService.confirm(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancel(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookingService.cancel(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}