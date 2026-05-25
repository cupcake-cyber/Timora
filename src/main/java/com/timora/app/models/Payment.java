package com.timora.app.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.timora.app.models.enums.PaymentMethod;
import com.timora.app.models.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // COMPANY
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // 🔥 CORREGIDO: booking, no appointment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    // AMOUNT
    @Column(name = "amount", nullable = false)
    private Double amount;

    // STATUS
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

    // METHOD
    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private PaymentMethod method;

    // CREATED AT
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}