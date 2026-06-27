//package com.timora.app.controller;
//
//import com.timora.app.model.Configuration;
//import com.timora.app.service.ConfigurationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/configurations")
//public class ConfigurationController {
//    private final ConfigurationService configurationService;
//
//    @GetMapping
//    public ResponseEntity<List<Configuration>> getAll() {
//        List<Configuration> configurations = configurationService.findAll();
//        return ResponseEntity.ok(configurations);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Configuration> getById(@PathVariable Long id) {
//        Configuration configuration = configurationService.findById(id);
//        return ResponseEntity.ok(configuration);
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<Configuration> getByUserId(@PathVariable Long userId) {
//        Configuration configuration = configurationService.findByUserId(userId);
//        return ResponseEntity.ok(configuration);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Configuration> update(@PathVariable Long id, @RequestBody Configuration configuration) {
//        Configuration updatedConfiguration = configurationService.update(id, configuration);
//        return ResponseEntity.ok(updatedConfiguration);
//    }
//
//    @PostMapping
//    public ResponseEntity<Configuration> create(@RequestBody Configuration configuration) {
//        Configuration savedConfiguration = configurationService.save(configuration);
//        return ResponseEntity.ok(savedConfiguration);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        configurationService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
//}