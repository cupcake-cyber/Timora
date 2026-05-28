package com.timora.app.dto;

import com.timora.app.model.enums.AvailabilityRecurring;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityCreateDTO {

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