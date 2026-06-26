package com.timora.app.controller;

import com.timora.app.dto.personidentity.PersonIdentityCreateDTO;
import com.timora.app.dto.personidentity.PersonIdentityDTO;
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


//    @GetMapping
//    public ResponseEntity<List<PersonIdentityDTO>> getAll() {
//        return ResponseEntity.ok(personManagementService.getAll());
//    }
//
//    @GetMapping("/{id}")
//    public PersonIdentityDTO getById(@PathVariable Long id) {
//        return personManagementService.getById(id);
//    }
//
//    @PatchMapping("/{id}")
//    public PersonIdentityDTO update(@PathVariable Long id,
//                                    @RequestBody UpdatePersonRequest request) {
//        return personManagementService.update(id, request);
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void delete(@PathVariable Long id) {
//        personManagementService.delete(id);
//    }


}