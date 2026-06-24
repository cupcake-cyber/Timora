package com.timora.app.controller;

import com.timora.app.dto.CreatePersonRequest;
import com.timora.app.dto.PersonResponseDTO;
import com.timora.app.dto.UpdatePersonRequest;
import com.timora.app.service.PersonIdentityService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonIdentityService personIdentityService;

    @GetMapping
    public ResponseEntity<List<PersonResponseDTO>> getAll() {
        return ResponseEntity.ok(personIdentityService.getAll());
    }

    @GetMapping("/{id}")
    public PersonResponseDTO getById(@PathVariable Long id) {
        return personIdentityService.getById(id);
    }

    @PatchMapping("/{id}")
    public PersonResponseDTO update(@PathVariable Long id,
                                    @RequestBody UpdatePersonRequest request) {
        return personIdentityService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        personIdentityService.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResponseDTO create(@RequestBody CreatePersonRequest request) {
        return personIdentityService.create(request);
    }
}