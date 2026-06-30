package com.timora.app.dto.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationPatchDTO {
    private Boolean notifyAppointments;
    private Boolean notifyReservations;
    private Boolean notifyCancellations;
    private Boolean notifyReminders;
    private Integer reminderMinutesBefore;
    private Boolean appChannelEnabled;
    private Boolean emailChannelEnabled;
    private LocalTime startTimeSilence;
    private LocalTime endTimeSilence;
    private Boolean darkMode;
}