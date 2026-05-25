package com.timora.app.service.impl;

import com.timora.app.models.Configuration;
import com.timora.app.repository.ConfigurationRepository;
import com.timora.app.service.ConfigurationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public List<Configuration> findAll() {
        return configurationRepository.findAll();
    }

    @Override
    public Configuration findById(Long id) {
        return configurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuration not found with id: " + id));
    }

    @Override
    public Configuration findByUserId(Long userId) {
        return configurationRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Configuration not found for user id: " + userId));
    }

    @Override
    public Configuration save(Configuration configuration) {
        return configurationRepository.save(configuration);
    }

    @Override
    public Configuration update(Long id, Configuration updatedConfiguration) {

        Configuration existingConfiguration = findById(id);

        existingConfiguration.setUser(updatedConfiguration.getUser());

        existingConfiguration.setNotifyAppointments(
                updatedConfiguration.getNotifyAppointments()
        );

        existingConfiguration.setNotifyReservations(
                updatedConfiguration.getNotifyReservations()
        );

        existingConfiguration.setNotifyCancellations(
                updatedConfiguration.getNotifyCancellations()
        );

        existingConfiguration.setNotifyReminders(
                updatedConfiguration.getNotifyReminders()
        );

        existingConfiguration.setReminderMinutesBefore(
                updatedConfiguration.getReminderMinutesBefore()
        );

        existingConfiguration.setAppChannelEnabled(
                updatedConfiguration.getAppChannelEnabled()
        );

        existingConfiguration.setEmailChannelEnabled(
                updatedConfiguration.getEmailChannelEnabled()
        );

        existingConfiguration.setStartTimeSilence(
                updatedConfiguration.getStartTimeSilence()
        );

        existingConfiguration.setEndTimeSilence(
                updatedConfiguration.getEndTimeSilence()
        );

        existingConfiguration.setDarkMode(
                updatedConfiguration.getDarkMode()
        );

        return configurationRepository.save(existingConfiguration);
    }

    @Override
    public void delete(Long id) {

        Configuration configuration = findById(id);

        configurationRepository.delete(configuration);
    }
}