package com.timora.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDetailsDTO {

    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private String specialty;

    private Boolean active;
}
