package com.timora.app.service.impl;

import com.timora.app.dto.customer.CustomerCreateDTO;
import com.timora.app.dto.customer.CustomerDTO;
import com.timora.app.exception.BusinessException;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.model.Customer;
import com.timora.app.model.Person;
import com.timora.app.model.User;
import com.timora.app.repository.CustomerRepository;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final SecurityHelper securityHelper;
    private final AccessControlService auth;

    @Override
    public Customer create(Person person, CustomerCreateDTO customerDTO) {

        User user = securityHelper.getCurrentUser();

        if (!auth.isOwner(user)) {
            if(user.getCompany().getId() != customerDTO.getCompanyId().longValue()){
                throw new ForbiddenException("You can not create entities outside of your company.");
            }
        }

        if (customerRepository.existsByPersonId(customerDTO.getPersonId())){
            throw new BusinessException("Customer already exists");
        }

        Customer customer = new Customer();

        customer.setPerson(person);
        customer.setCompany(person.getCompany());
        customer.setNotes(customerDTO.getNotes());

        return customerRepository.save(customer);
    }

//    @Override
//    public Customer updateCustomer(Person person, CreatePersonRequest.CustomerData data) {
//
//        Customer customer = customerRepository.findByPersonId(person.getId())
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//
//        if (data != null && data.getNotes() != null) {
//            customer.setNotes(data.getNotes());
//        }
//
//        return customerRepository.save(customer);
//    }
//
//    @Override
//    public void deleteByPersonId(Long personId) {
//        customerRepository.deleteByPersonId(personId);
//    }
//
//    @Override
//    public boolean existsByPerson(Long personId) {
//        return customerRepository.existsByPersonId(personId);
//    }

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