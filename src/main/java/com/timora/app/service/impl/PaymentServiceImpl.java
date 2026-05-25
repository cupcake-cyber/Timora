package com.timora.app.service.impl;

import java.time.LocalDateTime;
import com.timora.app.models.Payment;
import com.timora.app.repository.PaymentRepository;
import com.timora.app.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    public PaymentServiceImpl(PaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Payment> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Payment save(Payment payment) {
        payment.setCreatedAt(LocalDateTime.now());
        return repository.save(payment);
    }

    @Override
    public Payment update(Long id, Payment newPayment) {
        return repository.findById(id).map(existing -> {

            existing.setAmount(newPayment.getAmount());
            existing.setStatus(newPayment.getStatus());
            existing.setMethod(newPayment.getMethod());

            // 🔥 CORREGIDO
            existing.setBooking(newPayment.getBooking());
            existing.setCompany(newPayment.getCompany());

            return repository.save(existing);

        }).orElseThrow(() -> new RuntimeException("Payment no encontrado"));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Payment> findByBooking(Long bookingId) {
        return repository.findByBookingId(bookingId);
    }
}