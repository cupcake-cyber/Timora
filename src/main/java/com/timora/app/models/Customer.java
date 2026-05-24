package com.timora.app.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @OneToOne(fetch = FetchType.LAZY)
    @Column(name = "person_id", nullable = false)
    private Long personId;

    private String notes;

    @Column(name = "created_id")
    private LocalDate createdId;
}