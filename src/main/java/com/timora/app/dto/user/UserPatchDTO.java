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
public class UserPatchDTO {
    private String email;
    private GlobalRole role;
}
