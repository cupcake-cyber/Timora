package com.timora.app.dto.configuration;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ConfigurationCreateDTO {
    private Boolean notifyAppointments;
    private Boolean notifyReservations;
    private Boolean notifyCancellations;
    private Boolean notifyReminders;
    @Min(0)
    private Integer reminderMinutesBefore;
    private Boolean appChannelEnabled;
    private Boolean emailChannelEnabled;
    private LocalTime startTimeSilence;
    private LocalTime endTimeSilence;
    private Boolean darkMode;
}