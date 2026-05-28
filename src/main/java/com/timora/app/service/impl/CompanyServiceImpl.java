package com.timora.app.service.impl;

import com.timora.app.exception.BusinessException;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.exception.NotFoundException;
import com.timora.app.model.Company;
import com.timora.app.model.User;
import com.timora.app.model.enums.CompanyStatus;
import com.timora.app.model.enums.GlobalRole;
import com.timora.app.repository.CompanyRepository;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final SecurityHelper securityHelper;
    private final AccessControlService auth;

    @Override
    public Company createCompany(Company company) {

        User user = securityHelper.getCurrentUser();

        auth.requireOwner(user);

        if (companyRepository.existsByRuc(company.getRuc())) {
            throw new BusinessException("El RUC ya está registrado");
        }

        if (companyRepository.existsByEmail(company.getEmail())) {
            throw new BusinessException("El correo ya está registrado");
        }

        company.setStatus(CompanyStatus.ACTIVE);
        return companyRepository.save(company);
    }

    @Override
    public List<Company> getAllCompanies() {

        User user = securityHelper.getCurrentUser();

        if (auth.isOwner(user)) {
            return companyRepository.findAll();
        }

        return companyRepository.findAllById(
                List.of(user.getCompany().getId())
        );
    }

    @Override
    public Company getCompanyById(Long id) {

        User user = securityHelper.getCurrentUser();

        auth.requireSameCompany(user, id);

        return companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found"));
    }

    @Override
    public Company updateCompany(Long id, Company updatedCompany) {

        User user = securityHelper.getCurrentUser();

        auth.requireSameCompany(user, id);

        Company existing = getCompanyById(id);

        if (!existing.getEmail().equals(updatedCompany.getEmail())
                && companyRepository.existsByEmail(updatedCompany.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        if (!existing.getRuc().equals(updatedCompany.getRuc())
                && companyRepository.existsByRuc(updatedCompany.getRuc())) {
            throw new BusinessException("RUC already exists");
        }

        existing.setName(updatedCompany.getName());
        existing.setRuc(updatedCompany.getRuc());
        existing.setAddress(updatedCompany.getAddress());
        existing.setPhone(updatedCompany.getPhone());
        existing.setEmail(updatedCompany.getEmail());

        return companyRepository.save(existing);
    }

    @Override
    public void deleteCompanyById(Long id) {

        User user = securityHelper.getCurrentUser();

        auth.requireOwner(user);

        Company company = getCompanyById(id);

        company.setStatus(CompanyStatus.INACTIVE);
        companyRepository.save(company);
    }

    @Override
    public Company patchCompany(Long id, Company updatedCompany) {

        User user = securityHelper.getCurrentUser();

        // =========================
        // ACCESS CONTROL
        // =========================

        if (!auth.isOwner(user)) {

            if (user.getGlobalRole() == GlobalRole.USER) {
                throw new ForbiddenException("USER cannot update companies");
            }

            if (!user.getCompany().getId().equals(id)) {
                throw new ForbiddenException("ADMIN can only update their own company");
            }
        }

        Company existing = getCompanyById(id);

        // =========================
        // PATCH FIELDS
        // =========================

        if (updatedCompany.getName() != null) {
            existing.setName(updatedCompany.getName());
        }

        if (updatedCompany.getRuc() != null) {

            if (!existing.getRuc().equals(updatedCompany.getRuc())
                    && companyRepository.existsByRuc(updatedCompany.getRuc())) {
                throw new BusinessException("RUC ya registrado");
            }

            existing.setRuc(updatedCompany.getRuc());
        }

        if (updatedCompany.getAddress() != null) {
            existing.setAddress(updatedCompany.getAddress());
        }

        if (updatedCompany.getPhone() != null) {
            existing.setPhone(updatedCompany.getPhone());
        }

        if (updatedCompany.getEmail() != null) {

            if (!existing.getEmail().equals(updatedCompany.getEmail())
                    && companyRepository.existsByEmail(updatedCompany.getEmail())) {
                throw new BusinessException("Email ya registrado");
            }

            existing.setEmail(updatedCompany.getEmail());
        }

        return companyRepository.save(existing);
    }
}