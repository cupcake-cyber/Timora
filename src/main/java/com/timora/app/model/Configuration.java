package com.timora.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "configuration")
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "notify_appointments")
    private Boolean notifyAppointments;

    @Column(name = "notify_reservations")
    private Boolean notifyReservations;

    @Column(name = "notify_cancellations")
    private Boolean notifyCancellations;

    @Column(name = "notify_reminders")
    private Boolean notifyReminders;

    @Column(name = "reminder_minutes_before")
    private Integer reminderMinutesBefore;

    @Column(name = "app_channel_enabled")
    private Boolean appChannelEnabled;

    @Column(name = "email_channel_enabled")
    private Boolean emailChannelEnabled;

    @Column(name = "start_time_silence")
    private LocalTime startTimeSilence;

    @Column(name = "end_time_silence")
    private LocalTime endTimeSilence;

    @Column(name = "dark_mode")
    private Boolean darkMode;
}