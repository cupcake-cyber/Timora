package com.timora.app.dto.user;

import com.timora.app.model.enums.GlobalRole;
import com.timora.app.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private Long companyId;
    private String email;
    private GlobalRole role;
    private UserStatus status;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdDate;
}
