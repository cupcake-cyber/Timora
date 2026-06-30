package com.timora.app.service;

import com.timora.app.dto.configuration.ConfigurationDTO;
import com.timora.app.dto.configuration.ConfigurationPatchDTO;
import com.timora.app.model.Configuration;
import com.timora.app.model.User;

public interface ConfigurationService {

    ConfigurationDTO getMyConfiguration();
    public Configuration create(User user);
    ConfigurationDTO updateMyConfiguration(ConfigurationPatchDTO configuration);
}