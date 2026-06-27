package com.timora.app.dto.security;

import com.timora.app.model.enums.GlobalRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUser {

    // identidad base
    private Long userId;
    private String email;

    // tenant
    private Long companyId;

    // persona base
    private Long personId;

    // control global
    private GlobalRole role;
}