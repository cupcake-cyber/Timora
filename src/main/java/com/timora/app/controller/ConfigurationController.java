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

    @GetMapping("/user/{userId}")
    public ResponseEntity<ConfigurationDTO> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(configurationService.findByUserId(userId));
    }

    @PatchMapping("/user/{userId}")
    public ResponseEntity<ConfigurationDTO> update(@PathVariable Long userId, @RequestBody ConfigurationPatchDTO configuration) {
        return ResponseEntity.ok(configurationService.patch(userId, configuration));
    }
}