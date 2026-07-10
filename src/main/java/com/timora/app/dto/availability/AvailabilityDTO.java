package com.timora.app.dto.availability;

import com.timora.app.model.enums.AvailabilityRecurring;
import com.timora.app.model.enums.AvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityDTO {
    private Long id;
    private Long companyId;
    private Long supplierId;
    private LocalDate startDate;
    private LocalDate endDate;

    // Horario
    private LocalTime startTime;
    private LocalTime endTime;

    // Días de la semana como booleanos
    private Boolean monday;
    private Boolean tuesday;
    private Boolean wednesday;
    private Boolean thursday;
    private Boolean friday;
    private Boolean saturday;
    private Boolean sunday;

    // Configuración
    private AvailabilityRecurring recurrenceType;
    private Integer slotDurationMinutes;
    private Integer capacity;
    private AvailabilityStatus status;
    private String notes;
    private LocalDateTime createdAt;
}