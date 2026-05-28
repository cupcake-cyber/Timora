package com.timora.app.dto;

import com.timora.app.model.enums.AvailabilityRecurring;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AvailabilityCreateDTO {
    private Long supplierId;
    private LocalDate startDate;
    private LocalDate endDate;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private AvailabilityRecurring recurrenceType;
    private Integer slotDurationMinutes;
    private Integer capacity;
    private String notes;
}
