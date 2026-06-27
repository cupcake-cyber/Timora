package com.timora.app.dto.usersupplierpermission;

import com.timora.app.model.enums.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSupplierPermissionDTO {
    private Long userId;
    private Long supplierId;
    private Permission permission;
    private Long assignedByUserId;
    private LocalDateTime createdAt;
}