package com.timora.app.dto.usersupplierpermission;

import com.timora.app.model.enums.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSupplierPermissionCreateDTO {
    private Long userId;
    private Long supplierId;
    private Permission permission;
}