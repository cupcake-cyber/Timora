package com.timora.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class CalendarAvailabilityDTO {
    private LocalDate date;
    private List<LocalTime> availableSlots;
    private Integer remainingCapacity;
}
