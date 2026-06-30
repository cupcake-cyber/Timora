package com.timora.app.dto.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyCreateDTO {
    private String name;
    private String ruc;
    private String address;
    private String phone;
    private String email;
}