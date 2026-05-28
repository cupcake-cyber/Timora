package com.timora.app.service.impl;

import com.timora.app.dto.CreatePersonRequest;
import com.timora.app.model.Customer;
import com.timora.app.model.Person;
import com.timora.app.repository.CustomerRepository;
import com.timora.app.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Person person, CreatePersonRequest.CustomerData data) {

        if (customerRepository.existsByPersonId(person.getId())) {
            throw new IllegalArgumentException("Person already is customer");
        }

        Customer customer = new Customer();
        customer.setPerson(person);
        customer.setCompany(person.getCompany());

        if (data != null) {
            customer.setNotes(data.getNotes());
        }

        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Person person, CreatePersonRequest.CustomerData data) {

        Customer customer = customerRepository.findByPersonId(person.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (data != null && data.getNotes() != null) {
            customer.setNotes(data.getNotes());
        }

        return customerRepository.save(customer);
    }

    @Override
    public void deleteByPersonId(Long personId) {
        customerRepository.deleteByPersonId(personId);
    }

    @Override
    public boolean existsByPerson(Long personId) {
        return customerRepository.existsByPersonId(personId);
    }
}