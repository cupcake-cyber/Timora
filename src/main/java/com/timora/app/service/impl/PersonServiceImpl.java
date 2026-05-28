package com.timora.app.service.impl;

import com.timora.app.dto.CreatePersonRequest;
import com.timora.app.exception.BusinessException;
import com.timora.app.exception.NotFoundException;
import com.timora.app.model.Company;
import com.timora.app.model.Person;
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
    public Person createBasePerson(CreatePersonRequest request) {

        if (personRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Company not found"));

        Person person = new Person();
        person.setCompany(company);
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setPhone(request.getPhone());
        person.setEmail(request.getEmail());
        person.setAddress(request.getAddress());
        person.setStatus(PersonStatus.ACTIVE);

        return personRepository.save(person);
    }
}