package com.timora.app.service.impl;

import com.timora.app.models.Company;
import com.timora.app.models.enums.CompanyStatus;
import com.timora.app.repository.CompanyRepository;
import com.timora.app.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company createCompany(Company company) {
        if (companyRepository.existsByRuc(company.getRuc())) {
            throw new IllegalArgumentException("El RUC ya está registrado en el sistema.");
        }

        if (companyRepository.existsByEmail(company.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado en el sistema.");
        }

        company.setStatus(CompanyStatus.ACTIVE);
        return companyRepository.save(company);
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El id de la compañia no existe."));
    }

    @Override
    public Company updateCompany(Long id, Company updatedCompany) {
        Company existingCompany = getCompanyById(id);

        if (!existingCompany.getEmail().equals(updatedCompany.getEmail())
                && companyRepository.existsByEmail(updatedCompany.getEmail())) {

            throw new IllegalArgumentException("El correo ya está registrado");
        }

        if (!existingCompany.getRuc().equals(updatedCompany.getRuc())
                && companyRepository.existsByRuc(updatedCompany.getRuc())) {

            throw new IllegalArgumentException("El RUC ya está registrado");
        }

        existingCompany.setName(updatedCompany.getName());
        existingCompany.setRuc(updatedCompany.getRuc());
        existingCompany.setAddress(updatedCompany.getAddress());
        existingCompany.setPhone(updatedCompany.getPhone());
        existingCompany.setEmail(updatedCompany.getEmail());

        return companyRepository.save(existingCompany);
    }

    @Override
    public void deleteCompanyById(Long id) {
        Company company = getCompanyById(id);
        company.setStatus(CompanyStatus.INACTIVE);
        companyRepository.save(company);
    }
}
