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
    @NotNull
    private Long supplierId;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    private DayOfWeek dayOfWeek;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    @NotNull
    private AvailabilityRecurring recurrenceType;
    @NotNull
    @Min(5)
    @Max(480)
    private Integer slotDurationMinutes;
    @NotNull
    @Min(1)
    private Integer capacity;
    private String notes;
}
