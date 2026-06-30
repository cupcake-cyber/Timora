package com.timora.app.dto.personidentity;

import com.timora.app.dto.customer.CustomerDTO;
import com.timora.app.dto.person.PersonDTO;
import com.timora.app.dto.supplier.SupplierDTO;
import com.timora.app.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonIdentityDTO {
    private PersonDTO person;
    private UserDTO user;
    private CustomerDTO customer;
    private SupplierDTO supplier;
}