package com.timora.app.controller;

import com.timora.app.model.Configuration;
import com.timora.app.service.ConfigurationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configurations")
@CrossOrigin(origins = "*")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping
    public ResponseEntity<List<Configuration>> getAllConfigurations() {

        List<Configuration> configurations = configurationService.findAll();

        return ResponseEntity.ok(configurations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Configuration> getConfigurationById(
            @PathVariable Long id
    ) {

        Configuration configuration = configurationService.findById(id);

        return ResponseEntity.ok(configuration);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Configuration> getConfigurationByUserId(
            @PathVariable Long userId
    ) {

        Configuration configuration = configurationService.findByUserId(userId);

        return ResponseEntity.ok(configuration);
    }

    @PostMapping
    public ResponseEntity<Configuration> createConfiguration(
            @RequestBody Configuration configuration
    ) {

        Configuration savedConfiguration =
                configurationService.save(configuration);

        return new ResponseEntity<>(
                savedConfiguration,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Configuration> updateConfiguration(
            @PathVariable Long id,
            @RequestBody Configuration configuration
    ) {

        Configuration updatedConfiguration =
                configurationService.update(id, configuration);

        return ResponseEntity.ok(updatedConfiguration);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfiguration(
            @PathVariable Long id
    ) {

        configurationService.delete(id);

        return ResponseEntity.noContent().build();
    }
}