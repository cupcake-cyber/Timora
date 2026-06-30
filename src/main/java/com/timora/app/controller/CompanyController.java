package com.timora.app.controller;

import com.timora.app.dto.company.CompanyCreateDTO;
import com.timora.app.dto.company.CompanyDTO;
import com.timora.app.dto.company.CompanyPatchDTO;
import com.timora.app.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAll() {
        return ResponseEntity.ok(companyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CompanyDTO> patch(@PathVariable Long id, @RequestBody CompanyPatchDTO dto) {
        return ResponseEntity.ok(companyService.patch(id, dto));
    }

    @PostMapping
    public ResponseEntity<CompanyDTO> create(@RequestBody CompanyCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}