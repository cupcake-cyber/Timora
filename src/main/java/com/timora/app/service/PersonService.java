package com.timora.app.service;


import com.timora.app.dto.person.PersonCreateDTO;
import com.timora.app.dto.person.PersonPatchDTO;
import com.timora.app.model.Person;

public interface PersonService {
    Person create(PersonCreateDTO personDTO);
    Person findById(Long id);
    Person patch(Long id, PersonPatchDTO dto);
    void delete(Long id);
}