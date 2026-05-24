package com.timora.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "service")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // COMPANY
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // SUPPLIER
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    // NAME
    @Column(name = "name", nullable = false)
    private String name;

    // DESCRIPTION
    @Column(name = "description", nullable = false)
    private String description;

    // PRICE
    @Column(name = "price", nullable = false)
    private Double price;

    // DURATION
    @Column(name = "duration", nullable = false)
    private LocalTime duration;

    // STATUS
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ServiceStatus status;

    // CREATED AT
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // RELACIÓN CON APPOINTMENT
    @JsonIgnore
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    public Service(Company company, Supplier supplier, String name, String description,
                   Double price, LocalTime duration, ServiceStatus status, LocalDateTime createdAt) {
        this.company = company;
        this.supplier = supplier;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.status = status;
        this.createdAt = createdAt;
    }
}