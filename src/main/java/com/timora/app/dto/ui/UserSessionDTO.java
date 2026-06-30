package com.timora.app.dto.ui;

import com.timora.app.model.enums.GlobalRole;
import com.timora.app.model.enums.UiMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionDTO {
    private String firstName;
    private String lastName;
    private GlobalRole role;
    private UiMode mode;
}