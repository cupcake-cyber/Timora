package com.timora.app.controller;

import com.timora.app.dto.payment.PaymentCreateDTO;
import com.timora.app.dto.payment.PaymentDTO;
import com.timora.app.dto.payment.PaymentPatchDTO;
import com.timora.app.model.enums.PaymentStatus;
import com.timora.app.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDTO> create(@RequestBody PaymentCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.create(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentDTO> patch(
            @PathVariable Long id,
            @RequestBody PaymentPatchDTO dto) {
        return ResponseEntity.ok(paymentService.patch(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllByCompany() {
        return ResponseEntity.ok(paymentService.getAllByCompany());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentDTO> getByBookingId(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getByBookingId(bookingId));
    }

    @GetMapping("/status")
    public ResponseEntity<List<PaymentDTO>> getByStatus(@RequestParam PaymentStatus status) {
        return ResponseEntity.ok(paymentService.getByStatus(status));
    }
}