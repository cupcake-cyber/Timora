package com.timora.app.service;

import com.timora.app.dto.customer.CustomerCreateDTO;
import com.timora.app.model.Customer;
import com.timora.app.model.Person;

public interface CustomerService {

    Customer create(Person person, CustomerCreateDTO customer);

//    Customer updateCustomer(Person person, CreatePersonRequest.CustomerData data);
//
//    void deleteByPersonId(Long personId);
//
//    boolean existsByPerson(Long personId);
}