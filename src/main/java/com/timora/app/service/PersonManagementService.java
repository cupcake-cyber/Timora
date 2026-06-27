package com.timora.app.service;

import com.timora.app.dto.personidentity.PersonIdentityCreateDTO;
import com.timora.app.dto.personidentity.PersonIdentityDTO;
import com.timora.app.dto.personidentity.PersonIdentityPatchDTO;

public interface PersonManagementService {

    PersonIdentityDTO create(PersonIdentityCreateDTO request);
    PersonIdentityDTO patch(Long id, PersonIdentityPatchDTO request);
    void delete(Long personId);
//    List<PersonResponseDTO> getAll();
//    PersonResponseDTO getById(Long id);
}