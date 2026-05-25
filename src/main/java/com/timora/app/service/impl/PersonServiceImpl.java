package com.timora.app.service.impl;

import com.timora.app.model.Person;
import com.timora.app.model.enums.PersonStatus;
import com.timora.app.repository.PersonRepository;
import com.timora.app.service.PersonService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person createPerson(Person person) {
        if (personRepository.existsByEmail(person.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        person.setStatus(PersonStatus.ACTIVE);
        return personRepository.save(person);
    }

    @Override
    public List<Person> getAllPersons(Long companyId) {
        return personRepository.findByCompanyId(companyId);
    }

    @Override
    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
    }

    @Override
    public Person updatePerson(Long id, Person person) {
        Person existing = getPersonById(id);

        existing.setFirstName(person.getFirstName());
        existing.setLastName(person.getLastName());
        existing.setPhone(person.getPhone());
        existing.setEmail(person.getEmail());
        existing.setAddress(person.getAddress());

        return personRepository.save(existing);
    }

    @Override
    public void deletePerson(Long id) {
        Person person = getPersonById(id);

        person.setStatus(PersonStatus.INACTIVE);
        personRepository.save(person);
    }
}
