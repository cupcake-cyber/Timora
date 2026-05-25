package com.timora.app.service;

import com.timora.app.model.Configuration;

import java.util.List;

public interface ConfigurationService {

    List<Configuration> findAll();

    Configuration findById(Long id);

    Configuration findByUserId(Long userId);

    Configuration save(Configuration configuration);

    Configuration update(Long id, Configuration configuration);

    void delete(Long id);
}