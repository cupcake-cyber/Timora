package com.timora.app.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // COMPANY
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // APPOINTMENT
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Appointment appointment;

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
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Payment() {}
}