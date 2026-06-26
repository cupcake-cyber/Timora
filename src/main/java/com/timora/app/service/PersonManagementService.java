package com.timora.app.service;

import com.timora.app.dto.personidentity.PersonIdentityCreateDTO;
import com.timora.app.dto.personidentity.PersonIdentityDTO;

public interface PersonManagementService {

    PersonIdentityDTO create(PersonIdentityCreateDTO request);

//    List<PersonResponseDTO> getAll();
//
//    PersonResponseDTO getById(Long id);
//
//    PersonResponseDTO update(Long id, UpdatePersonRequest request);
//
//    void delete(Long id);
}