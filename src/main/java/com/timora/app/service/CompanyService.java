package com.timora.app.service;

import com.timora.app.dto.CompanyCreateDTO;
import com.timora.app.dto.CompanyDTO;
import com.timora.app.model.Company;

import java.util.List;

public interface CompanyService {
    CompanyDTO create(CompanyCreateDTO company);
    List<CompanyDTO> getAll();
    CompanyDTO getById(Long id);
    CompanyDTO patch(Long id, CompanyDTO company);
    void delete(Long id);
}