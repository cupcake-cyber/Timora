package com.timora.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponseDTO {

    private Long id;

    // PERSON
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private String status;

    // USER
    private Boolean hasUser;
    private UserDTO user;

    // CUSTOMER
    private Boolean isCustomer;
    private CustomerDTO customer;

    // SUPPLIER
    private Boolean isSupplier;
    private SupplierDTO supplier;

    // =========================

    @Getter
    @Setter
    public static class UserDTO {
        private Long id;
        private String loginEmail;
        private String globalRole;
        private String status;
    }

    @Getter
    @Setter
    public static class CustomerDTO {
        private Long id;
        private String notes;
    }

    @Getter
    @Setter
    public static class SupplierDTO {
        private Long id;
        private String specialty;
        private String notes;
    }
}