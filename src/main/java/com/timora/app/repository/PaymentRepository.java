package com.timora.app.repository;

import com.timora.app.model.Payment;
import com.timora.app.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Busca pagos de una compañía
     */
    List<Payment> findByCompanyId(Long companyId);

    /**
     * Busca pagos por estado
     */
    List<Payment> findByStatus(PaymentStatus status);

    /**
     * Busca pagos de una compañía por estado
     */
    List<Payment> findByCompanyIdAndStatus(Long companyId, PaymentStatus status);

    /**
     * Busca pagos de un booking específico
     */
    Payment findByBookingId(Long bookingId);

    /**
     * Verifica si un booking ya tiene un pago activo
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Payment p WHERE p.booking.id = :bookingId " +
            "AND p.status IN :activeStatuses")
    boolean existsByBookingIdAndStatusIn(
            @Param("bookingId") Long bookingId,
            @Param("activeStatuses") List<PaymentStatus> activeStatuses
    );

    /**
     * Obtiene todos los pagos de una compañía con sus bookings
     */
    @Query("SELECT p FROM Payment p JOIN FETCH p.booking WHERE p.company.id = :companyId")
    List<Payment> findByCompanyIdWithBooking(@Param("companyId") Long companyId);
}