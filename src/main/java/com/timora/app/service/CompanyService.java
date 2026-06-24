package com.timora.app.service;

import com.timora.app.model.Company;

import java.util.List;

public interface CompanyService {
    Company create(Company company);
    List<Company> getAll();
    Company getById(Long id);
    Company patch(Long id, Company company);
    void delete(Long id);
}