package com.timora.app.dto.security;

import com.timora.app.model.enums.GlobalRole;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserDTO {

    private Long id;

    private String email;

    private String fullName;

    private GlobalRole globalRole;

    private boolean active;

    private boolean companyAdmin;

    private boolean supplierUser;

    private Long companyId;

    private List<Long> supplierIds;

    private String status;

    private Map<Long, List<String>> supplierPermissions;
}