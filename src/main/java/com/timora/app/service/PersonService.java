package com.timora.app.service;


import com.timora.app.dto.person.PersonCreateDTO;
import com.timora.app.model.Person;

public interface PersonService {
    Person create(PersonCreateDTO personDTO);
}