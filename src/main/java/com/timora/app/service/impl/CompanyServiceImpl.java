package com.timora.app.service.impl;

import com.timora.app.dto.company.CompanyCreateDTO;
import com.timora.app.dto.company.CompanyDTO;
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
    public CompanyDTO create(CompanyCreateDTO companyDTO) {

        User user = securityHelper.getCurrentUser();
        auth.requireOwner(user);

        if (companyRepository.existsByRuc(companyDTO.getRuc())) {
            throw new BusinessException("The RUC already exists");
        }

        if (companyRepository.existsByEmail(companyDTO.getEmail())) {
            throw new BusinessException("The email already exists");
        }

        Company company = new Company();

        company.setRuc(companyDTO.getRuc());
        company.setEmail(companyDTO.getEmail());
        company.setName(companyDTO.getName());
        company.setAddress(companyDTO.getAddress());
        company.setPhone(companyDTO.getPhone());
        company.setStatus(CompanyStatus.ACTIVE);

        Company saved = companyRepository.save(company);

        return toDTO(saved);
    }

    @Override
    public List<CompanyDTO> getAll() {

        User user = securityHelper.getCurrentUser();
        auth.requireOwner(user);

        List<Company> companies = companyRepository.findByStatus(CompanyStatus.ACTIVE);

        return companies.stream().map(this::toDTO).toList();
    }

    @Override
    public CompanyDTO getById(Long id) {

        User user = securityHelper.getCurrentUser();
        auth.requireSameCompany(user, id);

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        return toDTO(company);
    }

    @Override
    public void delete(Long id) {

        User user = securityHelper.getCurrentUser();
        auth.requireOwner(user);

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        company.setStatus(CompanyStatus.INACTIVE);
        companyRepository.save(company);
    }

    @Override
    public CompanyDTO patch(Long id, CompanyDTO updatedCompany) {

        User user = securityHelper.getCurrentUser();

        if (!auth.isOwner(user)) {

            if (user.getGlobalRole() == GlobalRole.USER) {
                throw new ForbiddenException("USER cannot update companies");
            }

            if (user.getCompany() == null ||
                    !user.getCompany().getId().equals(id)) {
                throw new ForbiddenException("ADMIN can only update their own company");
            }
        }

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company no encontrada"));

        if (updatedCompany.getName() != null) {
            company.setName(updatedCompany.getName());
        }

        if (updatedCompany.getRuc() != null) {

            if (company.getRuc() != null &&
                    !company.getRuc().equals(updatedCompany.getRuc()) &&
                    companyRepository.existsByRuc(updatedCompany.getRuc())) {
                throw new BusinessException("RUC ya registrado");
            }

            company.setRuc(updatedCompany.getRuc());
        }

        if (updatedCompany.getAddress() != null) {
            company.setAddress(updatedCompany.getAddress());
        }

        if (updatedCompany.getPhone() != null) {
            company.setPhone(updatedCompany.getPhone());
        }

        if (updatedCompany.getEmail() != null) {

            if (company.getEmail() != null &&
                    !company.getEmail().equals(updatedCompany.getEmail()) &&
                    companyRepository.existsByEmail(updatedCompany.getEmail())) {
                throw new BusinessException("Email ya registrado");
            }

            company.setEmail(updatedCompany.getEmail());
        }

        Company saved = companyRepository.save(company);

        return toDTO(saved);
    }

    private CompanyDTO toDTO(Company company) {

        CompanyDTO dto = new CompanyDTO();

        dto.setId(company.getId());
        dto.setRuc(company.getRuc());
        dto.setEmail(company.getEmail());
        dto.setName(company.getName());
        dto.setAddress(company.getAddress());
        dto.setPhone(company.getPhone());
        dto.setStatus(company.getStatus());

        return dto;
    }
}