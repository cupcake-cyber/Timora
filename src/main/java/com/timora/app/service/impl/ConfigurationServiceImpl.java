package com.timora.app.service.impl;

import com.timora.app.dto.configuration.ConfigurationDTO;
import com.timora.app.dto.configuration.ConfigurationPatchDTO;
import com.timora.app.dto.security.CurrentUser;
import com.timora.app.model.Configuration;
import com.timora.app.model.User;
import com.timora.app.repository.ConfigurationRepository;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.ConfigurationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@AllArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final SecurityHelper securityHelper;
    @Override
    public ConfigurationDTO getMyConfiguration() {
        CurrentUser user = securityHelper.getCurrentUser();

        Configuration saved = configurationRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Configuration not found"));
        return toDTO(saved);
    }

    @Override
    public Configuration create(User user) {

        Configuration configuration = new Configuration();

        // USER
        configuration.setUser(user);

        // NOTIFICATIONS DEFAULTS
        configuration.setNotifyAppointments(true);
        configuration.setNotifyReservations(true);
        configuration.setNotifyCancellations(true);
        configuration.setNotifyReminders(true);

        // CHANNELS DEFAULTS
        configuration.setAppChannelEnabled(true);
        configuration.setEmailChannelEnabled(true);

        // DEFAULT REMINDER CONFIG
        configuration.setReminderMinutesBefore(30);

        // SILENCE TIME DEFAULTS (puedes ajustar)
        configuration.setStartTimeSilence(LocalTime.of(22, 0));
        configuration.setEndTimeSilence(LocalTime.of(7, 0));

        // UI SETTINGS DEFAULTS
        configuration.setDarkMode(false);

        return configurationRepository.save(configuration);
    }

    @Override
    @Transactional
    public ConfigurationDTO updateMyConfiguration(ConfigurationPatchDTO updatedConfiguration) {
        CurrentUser user = securityHelper.getCurrentUser();

        Configuration existingConfiguration = configurationRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Configuration not found"));

        if (updatedConfiguration.getNotifyAppointments() != null) {
            existingConfiguration.setNotifyAppointments(updatedConfiguration.getNotifyAppointments());
        }

        if (updatedConfiguration.getNotifyReservations() != null) {
            existingConfiguration.setNotifyReservations(updatedConfiguration.getNotifyReservations());
        }

        if (updatedConfiguration.getNotifyCancellations() != null) {
            existingConfiguration.setNotifyCancellations(updatedConfiguration.getNotifyCancellations());
        }

        if (updatedConfiguration.getNotifyReminders() != null) {
            existingConfiguration.setNotifyReminders(updatedConfiguration.getNotifyReminders());
        }

        if (updatedConfiguration.getReminderMinutesBefore() != null) {
            existingConfiguration.setReminderMinutesBefore(updatedConfiguration.getReminderMinutesBefore());
        }

        if (updatedConfiguration.getAppChannelEnabled() != null) {
            existingConfiguration.setAppChannelEnabled(updatedConfiguration.getAppChannelEnabled());
        }

        if (updatedConfiguration.getEmailChannelEnabled() != null) {
            existingConfiguration.setEmailChannelEnabled(updatedConfiguration.getEmailChannelEnabled());
        }

        if (updatedConfiguration.getStartTimeSilence() != null) {
            existingConfiguration.setStartTimeSilence(updatedConfiguration.getStartTimeSilence());
        }

        if (updatedConfiguration.getEndTimeSilence() != null) {
            existingConfiguration.setEndTimeSilence(updatedConfiguration.getEndTimeSilence());
        }

        if (updatedConfiguration.getDarkMode() != null) {
            existingConfiguration.setDarkMode(updatedConfiguration.getDarkMode());
        }

        Configuration saved = configurationRepository.save(existingConfiguration);
        return toDTO(saved);
    }

    private ConfigurationDTO toDTO(Configuration configuration) {
        ConfigurationDTO dto = new ConfigurationDTO();

        dto.setId(configuration.getId());
        dto.setNotifyAppointments(configuration.getNotifyAppointments());
        dto.setNotifyReservations(configuration.getNotifyReservations());
        dto.setNotifyCancellations(configuration.getNotifyCancellations());
        dto.setNotifyReminders(configuration.getNotifyReminders());
        dto.setReminderMinutesBefore(configuration.getReminderMinutesBefore());
        dto.setAppChannelEnabled(configuration.getAppChannelEnabled());
        dto.setEmailChannelEnabled(configuration.getEmailChannelEnabled());
        dto.setStartTimeSilence(configuration.getStartTimeSilence());
        dto.setEndTimeSilence(configuration.getEndTimeSilence());
        dto.setDarkMode(configuration.getDarkMode());

        return dto;
    }


}