package com.timora.app.controller;

import com.timora.app.dto.configuration.ConfigurationDTO;
import com.timora.app.dto.configuration.ConfigurationPatchDTO;
import com.timora.app.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/configurations")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @GetMapping
    public ResponseEntity<ConfigurationDTO> getMyConfiguration() {
        return ResponseEntity.ok(configurationService.getMyConfiguration());
    }

    @PatchMapping
    public ResponseEntity<ConfigurationDTO> updateMyConfiguration(
            @RequestBody ConfigurationPatchDTO configuration
    ) {
        return ResponseEntity.ok(configurationService.updateMyConfiguration(configuration));
    }
}