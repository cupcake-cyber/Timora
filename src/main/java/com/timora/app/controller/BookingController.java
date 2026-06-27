//package com.timora.app.controller;
//
//import com.timora.app.dto.BookingCreateDTO;
//import com.timora.app.dto.BookingDetailsDTO;
//import com.timora.app.dto.BookingSummaryDTO;
//import com.timora.app.model.Booking;
//import com.timora.app.service.BookingService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/bookings")
//@RequiredArgsConstructor
//public class BookingController {
//
//    private final BookingService bookingService;
//
//    // =========================
//    // CREATE (SUPPLIER ONLY)
//    // =========================
//    @PostMapping
//    public ResponseEntity<BookingDetailsDTO> create(
//            @RequestBody BookingCreateDTO dto
//    ) {
//        return ResponseEntity.ok(
//                bookingService.create(dto)
//        );
//    }
//
//    // =========================
//    // GET MY BOOKINGS (SUPPLIER)
//    // =========================
//    @GetMapping("/me")
//    public ResponseEntity<List<BookingSummaryDTO>> getMyBookings() {
//        return ResponseEntity.ok(
//                bookingService.getMyBookings()
//        );
//    }
//
//    // =========================
//    // GET BY ID (SAFE VIEW)
//    // =========================
//    @GetMapping("/{id}")
//    public ResponseEntity<BookingDetailsDTO> getById(@PathVariable Long id) {
//        return ResponseEntity.ok(
//                bookingService.getById(id)
//        );
//    }
//
//    // =========================
//    // STATUS ACTIONS (OWNER OF BOOKING ONLY)
//    // =========================
//    @PutMapping("/{id}/confirm")
//    public ResponseEntity<BookingDetailsDTO> confirm(@PathVariable Long id) {
//        return ResponseEntity.ok(
//                bookingService.confirm(id)
//        );
//    }
//
//    @PutMapping("/{id}/cancel")
//    public ResponseEntity<BookingDetailsDTO> cancel(@PathVariable Long id) {
//        return ResponseEntity.ok(
//                bookingService.cancel(id)
//        );
//    }
//
//    @PutMapping("/{id}/complete")
//    public ResponseEntity<BookingDetailsDTO> complete(@PathVariable Long id) {
//        return ResponseEntity.ok(
//                bookingService.complete(id)
//        );
//    }
//
//    // =========================
//    // DELETE (SOFT RECOMMENDED)
//    // =========================
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        bookingService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
//}