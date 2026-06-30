package com.timora.app.dto.personidentity;

import com.timora.app.dto.customer.CustomerPatchDTO;
import com.timora.app.dto.person.PersonPatchDTO;
import com.timora.app.dto.supplier.SupplierPatchDTO;
import com.timora.app.dto.user.UserPatchDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonIdentityPatchDTO {
    private PersonPatchDTO person;
    private UserPatchDTO user;
    private CustomerPatchDTO customer;
    private SupplierPatchDTO supplier;
}