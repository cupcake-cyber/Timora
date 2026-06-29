package com.timora.app.service.impl;

import com.timora.app.dto.security.CurrentUser;
import com.timora.app.dto.ui.UserSessionDTO;
import com.timora.app.model.Person;
import com.timora.app.model.enums.GlobalRole;
import com.timora.app.model.enums.UiMode;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.PersonService;
import com.timora.app.service.SessionService;
import com.timora.app.service.UserSupplierPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SecurityHelper securityHelper;
    private final PersonService personService;
    private final UserSupplierPermissionService userSupplierPermissionService;
    @Override
    public UserSessionDTO getCurrentSession() {

        CurrentUser current = securityHelper.getCurrentUser();

        Person p = personService.findById(current.getPersonId());
        boolean hasPermissions = userSupplierPermissionService.hasAnyPermission(p.getUser().getId());

        UiMode ui;

        if (p.getUser().getRole() == GlobalRole.OWNER) {
            ui = UiMode.OWNER;

        } else if (p.getUser().getRole() == GlobalRole.ADMIN && p.getSupplier() != null) {
            ui = UiMode.ADMIN_SUPPLIER;

        } else if (p.getUser().getRole() == GlobalRole.ADMIN) {
            ui = UiMode.ADMIN;

        } else if (p.getUser().getRole() == GlobalRole.USER
                && p.getSupplier() != null
                && hasPermissions) {

            ui = UiMode.USER_PERMISSION_SUPPLIER;

        } else if (p.getUser().getRole() == GlobalRole.USER
                && hasPermissions) {

            ui = UiMode.USER_PERMISSION;

        } else if (p.getUser().getRole() == GlobalRole.USER
                && p.getSupplier() != null) {

            ui = UiMode.USER_SUPPLIER;

        } else {
            ui = UiMode.USER_PERMISSION;
        }

        UserSessionDTO dto = new UserSessionDTO();

        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setRole(p.getUser().getRole());
        dto.setMode(ui);
        return dto;
    }

}