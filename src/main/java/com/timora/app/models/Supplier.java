package com.timora.app.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "supplier")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @OneToOne
    @Column(name = "person_id", nullable = false)
    private Long personId;

    @Column(nullable = false)
    private String specialty;

    private String notes;

    @Column(name = "company_at")
    private LocalDate companyAt;
}