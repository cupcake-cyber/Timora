package com.timora.app.security;

import com.timora.app.exception.ForbiddenException;
import com.timora.app.model.User;
import com.timora.app.model.enums.GlobalRole;
import org.springframework.stereotype.Component;

@Component
public class AccessControlService {

    public void requireOwner(User user) {
        if (user.getGlobalRole() != GlobalRole.OWNER) {
            throw new ForbiddenException("Only OWNER allowed");
        }
    }

    public void requireAdminOrOwner(User user) {
        if (user.getGlobalRole() == GlobalRole.USER) {
            throw new ForbiddenException("Insufficient permissions");
        }
    }

    public void requireSameCompany(User user, Long companyId) {

        if (user.getGlobalRole() == GlobalRole.OWNER) return;

        if (user.getCompany() == null ||
                !user.getCompany().getId().equals(companyId)) {

            throw new ForbiddenException("Different company access denied");
        }
    }

    public boolean isOwner(User user) {
        return user.getGlobalRole() == GlobalRole.OWNER;
    }
}