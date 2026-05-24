package com.timora.app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;
import java.time.LocalTime;

@Entity
@Table(name = "configuration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "actives")
    private Boolean actives;

    @Column(name = "reservations")
    private Boolean reservations;

    @Column(name = "cancellations")
    private Boolean cancellations;

    @Column(name = "reminders")
    private Boolean reminders;

    @Column(name = "minutes_ahead_reminder")
    private Integer minutesAheadReminder;

    @Column(name = "app_channel")
    private Boolean appChannel;

    @Column(name = "email_channel")
    private Boolean emailChannel;

    @Column(name = "start_time_silence")
    private LocalTime startTimeSilence;

    @Column(name = "end_time_silence")
    private LocalTime endTimeSilence;

    @Column(name = "dark_mode")
    private Boolean darkMode;
}