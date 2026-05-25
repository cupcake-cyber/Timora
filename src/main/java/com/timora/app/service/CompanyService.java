package com.timora.app.service;

import com.timora.app.model.Company;

import java.util.List;

public interface CompanyService {
    Company createCompany(Company company);
    List<Company> getAllCompanies();
    Company getCompanyById(Long id);
    Company updateCompany(Long id, Company company);
    void deleteCompanyById(Long id);
}
