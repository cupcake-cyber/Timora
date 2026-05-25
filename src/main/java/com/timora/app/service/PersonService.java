package com.timora.app.service;

import com.timora.app.models.Person;

import java.util.List;

public interface PersonService {
    Person createPerson(Person person);
    List<Person> getAllPersons(Long companyId);
    Person getPersonById(Long id);
    Person updatePerson(Long id, Person person);
    void deletePerson(Long id);
}
