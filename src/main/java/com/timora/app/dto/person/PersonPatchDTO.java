package com.timora.app.dto.person;

import com.timora.app.model.enums.PersonStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonPatchDTO {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private PersonStatus status;
}