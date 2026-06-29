package com.timora.app.dto.usersupplierpermission;

import com.timora.app.model.enums.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPermissionMapDTO {

    private Map<Long, Set<Permission>> permissions;

}