package com.timora.app.controller;

import com.timora.app.dto.personidentity.PersonIdentityCreateDTO;
import com.timora.app.dto.personidentity.PersonIdentityDTO;
import com.timora.app.dto.personidentity.PersonIdentityPatchDTO;
import com.timora.app.service.PersonManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/persons")
public class PersonManagementController {

    private final PersonManagementService personManagementService;

    @PostMapping
    public ResponseEntity<PersonIdentityDTO> create(@RequestBody PersonIdentityCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personManagementService.create(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PersonIdentityDTO> patch(@PathVariable Long id,@RequestBody PersonIdentityPatchDTO dto) {
        return ResponseEntity.ok(personManagementService.patch(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personManagementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PersonIdentityDTO>> getAll() {
        return ResponseEntity.ok(personManagementService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonIdentityDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(personManagementService.getById(id));
    }


}