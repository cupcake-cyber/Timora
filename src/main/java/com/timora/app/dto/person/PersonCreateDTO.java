package com.timora.app.dto.person;

import com.timora.app.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonCreateDTO {
    private Long companyId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
}
