package com.timora.app.service;

import com.timora.app.dto.personidentity.PersonIdentityCreateDTO;
import com.timora.app.dto.personidentity.PersonIdentityDTO;
import com.timora.app.dto.personidentity.PersonIdentityPatchDTO;

import java.util.List;

public interface PersonManagementService {
    PersonIdentityDTO create(PersonIdentityCreateDTO request);
    PersonIdentityDTO patch(Long id, PersonIdentityPatchDTO request);
    void delete(Long personId);
    List<PersonIdentityDTO> getAll();
    PersonIdentityDTO getById(Long personId);

}