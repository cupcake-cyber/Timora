package com.timora.app.service;

import com.timora.app.dto.CreatePersonRequest;
import com.timora.app.dto.PersonResponseDTO;
import com.timora.app.dto.UpdatePersonRequest;

import java.util.List;

public interface PersonIdentityService {

    PersonResponseDTO create(CreatePersonRequest request);

    List<PersonResponseDTO> getAll();

    PersonResponseDTO getById(Long id);

    PersonResponseDTO update(Long id, UpdatePersonRequest request);

    void delete(Long id);
}