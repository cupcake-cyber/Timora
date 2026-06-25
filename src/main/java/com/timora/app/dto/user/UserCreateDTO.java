package com.timora.app.dto.user;

import com.timora.app.model.enums.GlobalRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    private Long companyId;
    private String email;
    private String password;
    private GlobalRole role;
}
