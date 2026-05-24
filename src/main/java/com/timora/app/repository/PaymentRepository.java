package com.timora.app.repository;

import com.timora.app.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Buscar pagos por appointment
    List<Payment> findByAppointmentId(Long appointmentId);
}