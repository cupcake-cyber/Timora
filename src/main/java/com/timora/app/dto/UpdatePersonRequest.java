package com.timora.app.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePersonRequest {

    // PERSON
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;

    // USER
    private Boolean updateUser;
    private CreatePersonRequest.UserData user;

    // CUSTOMER
    private Boolean updateCustomer;
    private CreatePersonRequest.CustomerData customer;

    // SUPPLIER
    private Boolean updateSupplier;
    private CreatePersonRequest.SupplierData supplier;
}