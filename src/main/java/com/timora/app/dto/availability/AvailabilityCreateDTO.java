package com.timora.app.dto.availability;

import com.timora.app.model.enums.AvailabilityRecurring;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityCreateDTO {
    private Long companyId;
    private Long supplierId;
    private LocalDate startDate;
    private LocalDate endDate;

    // Horario
    private LocalTime startTime;
    private LocalTime endTime;

    // Días de la semana como booleanos
    private Boolean monday = false;
    private Boolean tuesday = false;
    private Boolean wednesday = false;
    private Boolean thursday = false;
    private Boolean friday = false;
    private Boolean saturday = false;
    private Boolean sunday = false;

    // Configuración
    private AvailabilityRecurring recurrenceType;
    private Integer slotDurationMinutes;
    private Integer capacity;
    private String notes;
}