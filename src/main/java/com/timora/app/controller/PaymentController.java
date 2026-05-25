package com.timora.app.controller;

import com.timora.app.model.Payment;
import com.timora.app.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Payment> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Payment obtener(@PathVariable Long id) {
        return service.findById(id).orElse(null);
    }

    @PostMapping
    public Payment crear(@RequestBody Payment payment) {
        return service.save(payment);
    }

    @PutMapping("/{id}")
    public Payment actualizar(@PathVariable Long id, @RequestBody Payment payment) {
        return service.update(id, payment);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.delete(id);
    }


    @GetMapping("/booking/{id}")
    public List<Payment> porBooking(@PathVariable Long id) {
        return service.findByBooking(id);
    }
}
