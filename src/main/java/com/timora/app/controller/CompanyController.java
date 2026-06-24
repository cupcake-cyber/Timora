package com.timora.app.controller;

import com.timora.app.model.Company;
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
    public ResponseEntity<List<Company>> getAll() {
        return ResponseEntity.ok(companyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Company> patch(@PathVariable Long id, @RequestBody Company company) {
        return ResponseEntity.ok(companyService.patch(id, company));
    }

    @PostMapping
    public ResponseEntity<Company> create(@RequestBody Company company) {
        Company createdCompany = companyService.create(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}