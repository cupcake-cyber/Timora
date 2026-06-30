    package com.timora.app.dto.personidentity;

    import com.timora.app.dto.customer.CustomerCreateDTO;
    import com.timora.app.dto.person.PersonCreateDTO;
    import com.timora.app.dto.supplier.SupplierCreateDTO;
    import com.timora.app.dto.user.UserCreateDTO;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class PersonIdentityCreateDTO {
        private PersonCreateDTO person;
        private UserCreateDTO user;
        private CustomerCreateDTO customer;
        private SupplierCreateDTO supplier;
    }