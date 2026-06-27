package com.timora.app.service;

import com.timora.app.dto.configuration.ConfigurationDTO;
import com.timora.app.model.Configuration;
import com.timora.app.model.User;

public interface ConfigurationService {
    ConfigurationDTO findByUserId(Long userId);

    Configuration create(User user);

    Configuration patch(Long id, Configuration configuration);

}