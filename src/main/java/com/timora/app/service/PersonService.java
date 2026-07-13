package com.timora.app.service;


import com.timora.app.dto.person.PersonCreateDTO;
import com.timora.app.dto.person.PersonPatchDTO;
import com.timora.app.model.Person;

import java.util.List;

public interface PersonService {
    Person create(PersonCreateDTO personDTO);
    Person findById(Long id);
    Person patch(Long id, PersonPatchDTO dto);
    void delete(Long id);
    List<Person> findAll();
    List<Person> findByCompanyId(Long companyId);
    Person getByIdEntity(Long id);
    List<Person> findByIds(List<Long> ids);
}