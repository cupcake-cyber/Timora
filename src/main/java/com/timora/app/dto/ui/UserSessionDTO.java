package com.timora.app.dto.ui;

import com.timora.app.model.enums.GlobalRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionDTO {

    private String fullName;
    private GlobalRole role;
}