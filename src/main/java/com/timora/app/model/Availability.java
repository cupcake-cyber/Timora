package com.timora.app.model;

import com.timora.app.model.enums.AvailabilityRecurring;
import com.timora.app.model.enums.AvailabilityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "availability")
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    // Días de la semana como booleanos
    @Column(name = "monday")
    private Boolean monday = false;

    @Column(name = "tuesday")
    private Boolean tuesday = false;

    @Column(name = "wednesday")
    private Boolean wednesday = false;

    @Column(name = "thursday")
    private Boolean thursday = false;

    @Column(name = "friday")
    private Boolean friday = false;

    @Column(name = "saturday")
    private Boolean saturday = false;

    @Column(name = "sunday")
    private Boolean sunday = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_type")
    private AvailabilityRecurring recurrenceType = AvailabilityRecurring.NONE;

    @Column(name = "slot_duration_minutes")
    private Integer slotDurationMinutes;

    @Column(name = "capacity")
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AvailabilityStatus status = AvailabilityStatus.ACTIVE;

    @Column(name = "notes")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Obtiene los días activos como Set de DayOfWeek
     */
    public Set<DayOfWeek> getActiveDays() {
        Set<DayOfWeek> activeDays = new HashSet<>();
        if (Boolean.TRUE.equals(monday)) activeDays.add(DayOfWeek.MONDAY);
        if (Boolean.TRUE.equals(tuesday)) activeDays.add(DayOfWeek.TUESDAY);
        if (Boolean.TRUE.equals(wednesday)) activeDays.add(DayOfWeek.WEDNESDAY);
        if (Boolean.TRUE.equals(thursday)) activeDays.add(DayOfWeek.THURSDAY);
        if (Boolean.TRUE.equals(friday)) activeDays.add(DayOfWeek.FRIDAY);
        if (Boolean.TRUE.equals(saturday)) activeDays.add(DayOfWeek.SATURDAY);
        if (Boolean.TRUE.equals(sunday)) activeDays.add(DayOfWeek.SUNDAY);
        return activeDays;
    }

    /**
     * Verifica si hay al menos un día seleccionado
     */
    public boolean hasAnyDaySelected() {
        return Boolean.TRUE.equals(monday) ||
                Boolean.TRUE.equals(tuesday) ||
                Boolean.TRUE.equals(wednesday) ||
                Boolean.TRUE.equals(thursday) ||
                Boolean.TRUE.equals(friday) ||
                Boolean.TRUE.equals(saturday) ||
                Boolean.TRUE.equals(sunday);
    }

    /**
     * Verifica si un día específico está activo
     */
    public boolean isDayActive(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> Boolean.TRUE.equals(monday);
            case TUESDAY -> Boolean.TRUE.equals(tuesday);
            case WEDNESDAY -> Boolean.TRUE.equals(wednesday);
            case THURSDAY -> Boolean.TRUE.equals(thursday);
            case FRIDAY -> Boolean.TRUE.equals(friday);
            case SATURDAY -> Boolean.TRUE.equals(saturday);
            case SUNDAY -> Boolean.TRUE.equals(sunday);
        };
    }
}