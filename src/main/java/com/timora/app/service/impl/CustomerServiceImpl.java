package com.timora.app.service.impl;

import com.timora.app.dto.customer.CustomerCreateDTO;
import com.timora.app.dto.customer.CustomerDTO;
import com.timora.app.dto.customer.CustomerPatchDTO;
import com.timora.app.model.Customer;
import com.timora.app.model.Person;
import com.timora.app.repository.CustomerRepository;
import com.timora.app.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer findById(Long id) {
        return  customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    @Override
    @Transactional
    public Customer create(Person person, CustomerCreateDTO customerDTO) {

        Customer customer = new Customer();
        customer.setPerson(person);
        customer.setCompany(person.getCompany());
        customer.setNotes(customerDTO.getNotes());

        return customerRepository.save(customer);
    }
    @Override
    @Transactional
    public Customer patch(Long id, CustomerPatchDTO dto) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        if (dto.getNotes() != null) {
            customer.setNotes(dto.getNotes());
        }

        return customer;
    }

    private CustomerDTO toDTO(Customer customer) {

        CustomerDTO dto = new CustomerDTO();

        dto.setId(customer.getId());
        dto.setCompanyId(customer.getCompany().getId());
        dto.setPersonId(customer.getPerson().getId());
        dto.setNotes(customer.getNotes());
        dto.setCreatedAt(customer.getCreatedAt());

        return dto;
    }
}