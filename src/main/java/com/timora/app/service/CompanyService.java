package com.timora.app.service;

import com.timora.app.dto.company.CompanyCreateDTO;
import com.timora.app.dto.company.CompanyDTO;

import java.util.List;

public interface CompanyService {
    CompanyDTO create(CompanyCreateDTO company);
    List<CompanyDTO> getAll();
    CompanyDTO getById(Long id);
    CompanyDTO patch(Long id, CompanyDTO company);
    void delete(Long id);
}