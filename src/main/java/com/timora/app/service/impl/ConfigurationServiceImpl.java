package com.timora.app.service.impl;

import com.timora.app.dto.configuration.ConfigurationDTO;
import com.timora.app.model.Configuration;
import com.timora.app.model.User;
import com.timora.app.repository.ConfigurationRepository;
import com.timora.app.service.ConfigurationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    @Override
    public ConfigurationDTO findByUserId(Long userId) {
        Configuration saved = configurationRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Configuration not found with User id: " + userId));
        return toDTO(saved);
    }

    @Override
    public Configuration create(User user) {
        Configuration configuration = new Configuration();
        configuration.setUser(user);
        return configurationRepository.save(configuration);
    }

    @Override
    @Transactional
    public Configuration patch(Long id, Configuration updatedConfiguration) {

        Configuration existingConfiguration = configurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuration not found with id: " + id));

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
        return configurationRepository.save(existingConfiguration);
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