package com.timora.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.timora.app.models.enums.ServiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;


    @Column(nullable = false)
    private Integer duration;

    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    @JsonIgnore
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
}