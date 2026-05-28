package com.timora.app.service;

import com.timora.app.dto.CreatePersonRequest;
import com.timora.app.model.Person;

public interface PersonService {

    Person createBasePerson(CreatePersonRequest request);
}