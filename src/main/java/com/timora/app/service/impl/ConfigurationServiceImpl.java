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
                .orElseThrow(() -> new RuntimeException("Configuration not found"));
    }

    @Override
    public Configuration findByUserId(Long userId) {
        return configurationRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Configuration not found for user"));
    }

    @Override
    public Configuration save(Configuration configuration) {
        return configurationRepository.save(configuration);
    }

    @Override
    public Configuration update(Long id, Configuration updated) {
        Configuration existing = findById(id);

        existing.setActives(updated.getActives());
        existing.setReservations(updated.getReservations());
        existing.setCancellations(updated.getCancellations());
        existing.setReminders(updated.getReminders());
        existing.setMinutesAheadReminder(updated.getMinutesAheadReminder());
        existing.setAppChannel(updated.getAppChannel());
        existing.setEmailChannel(updated.getEmailChannel());
        existing.setStartTimeSilence(updated.getStartTimeSilence());
        existing.setEndTimeSilence(updated.getEndTimeSilence());
        existing.setDarkMode(updated.getDarkMode());

        return configurationRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        configurationRepository.deleteById(id);
    }
}