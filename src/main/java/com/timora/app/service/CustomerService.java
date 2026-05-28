package com.timora.app.service;

import com.timora.app.dto.CreatePersonRequest;
import com.timora.app.model.Customer;
import com.timora.app.model.Person;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Customer createCustomer(Person person, CreatePersonRequest.CustomerData data);

    Customer updateCustomer(Person person, CreatePersonRequest.CustomerData data);

    void deleteByPersonId(Long personId);

    boolean existsByPerson(Long personId);
}