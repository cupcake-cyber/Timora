package com.timora.app.service.impl;

import com.timora.app.dto.person.PersonCreateDTO;
import com.timora.app.dto.person.PersonDTO;
import com.timora.app.exception.BusinessException;
import com.timora.app.model.Company;
import com.timora.app.model.Person;
import com.timora.app.model.enums.CompanyStatus;
import com.timora.app.model.enums.PersonStatus;
import com.timora.app.repository.CompanyRepository;
import com.timora.app.repository.PersonRepository;
import com.timora.app.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final CompanyRepository companyRepository;

    @Override
    public Person create(PersonCreateDTO personDTO) {

        if (personRepository.existsByEmail(personDTO.getEmail())) {
            throw new BusinessException("Person already exists");
        }

        Company company = companyRepository.findByIdAndStatus(personDTO.getCompanyId(), CompanyStatus.ACTIVE);

        Person person = new Person();
        person.setCompany(company);
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setPhone(personDTO.getPhone());
        person.setEmail(personDTO.getEmail());
        person.setAddress(personDTO.getAddress());
        person.setStatus(PersonStatus.ACTIVE);

        return personRepository.save(person);
    }
    private PersonDTO toDTO(Person person) {

        PersonDTO dto = new PersonDTO();

        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setPhone(person.getPhone());
        dto.setEmail(person.getEmail());
        dto.setAddress(person.getAddress());
        dto.setCompanyId(person.getCompany().getId());
        dto.setStatus(person.getStatus());

        return dto;
    }
}