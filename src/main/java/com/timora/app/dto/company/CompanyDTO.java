package com.timora.app.dto.company;

import com.timora.app.model.enums.CompanyStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompanyDTO {
    private Long id;
    private String name;
    private String ruc;
    private String address;
    private String phone;
    private String email;
    private CompanyStatus status;
}