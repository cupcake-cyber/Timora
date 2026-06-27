package com.timora.app.service;

import com.timora.app.dto.customer.CustomerCreateDTO;
import com.timora.app.dto.customer.CustomerDTO;
import com.timora.app.dto.customer.CustomerPatchDTO;
import com.timora.app.model.Customer;
import com.timora.app.model.Person;

public interface CustomerService {
    Customer create(Person person, CustomerCreateDTO customer);
    Customer findById(Long id);
    Customer patch(Long id, CustomerPatchDTO dto);
}