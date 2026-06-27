package com.timora.app.dto.security;

import com.timora.app.model.enums.GlobalRole;
import com.timora.app.model.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserDTO {
    //Tenant
    private Long companyId;
    //Datos Person
    private Long personId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    //Datos User
    private Long userId;
    private String email;
    private GlobalRole role;
    private UserStatus status;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdDate;
    //Datos Supplier
    private Long supplierId;
    private String specialty;
    private String notes;
    //Datos Customer
    //private Long customerId
    //private String notes;
}