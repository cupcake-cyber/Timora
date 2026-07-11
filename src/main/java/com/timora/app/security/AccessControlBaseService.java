package com.timora.app.security;

import com.timora.app.dto.security.CurrentUser;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.model.Supplier;
import com.timora.app.model.User;
import com.timora.app.model.enums.GlobalRole;
import org.springframework.stereotype.Component;

@Component
public class AccessControlBaseService {

    // =========================
    // ROLES BASE
    // =========================

    public boolean isOwner(User user) {
        return user.getRole() == GlobalRole.OWNER;
    }

    public boolean isOwner(CurrentUser user) {
        return user.getRole() == GlobalRole.OWNER;
    }

    public boolean isAdmin(User user) {
        return user.getRole() == GlobalRole.ADMIN;
    }

    public boolean isAdmin(CurrentUser user) {
        return user.getRole() == GlobalRole.ADMIN;
    }

    public boolean isUser(User user) {
        return user.getRole() == GlobalRole.USER;
    }

    public boolean isUser(CurrentUser user) {
        return user.getRole() == GlobalRole.USER;
    }

    public boolean isAdminOrOwner(User user) {
        return isAdmin(user) || isOwner(user);
    }

    public boolean isAdminOrOwner(CurrentUser user) {
        return isAdmin(user) || isOwner(user);
    }

    // =========================
    // ROLES - EXCEPTIONS
    // =========================

    public void requireOwner(User user) {
        if (!isOwner(user)) {
            throw new ForbiddenException("Only OWNER allowed");
        }
    }

    public void requireOwner(CurrentUser user) {
        if (!isOwner(user)) {
            throw new ForbiddenException("Only OWNER allowed");
        }
    }

    public void requireAdmin(User user) {
        if (!isAdmin(user)) {
            throw new ForbiddenException("Only ADMIN allowed");
        }
    }

    public void requireAdmin(CurrentUser user) {
        if (!isAdmin(user)) {
            throw new ForbiddenException("Only ADMIN allowed");
        }
    }

    public void requireAdminOrOwner(User user) {
        if (!isAdminOrOwner(user)) {
            throw new ForbiddenException("Only ADMIN or OWNER allowed");
        }
    }

    public void requireAdminOrOwner(CurrentUser user) {
        if (!isAdminOrOwner(user)) {
            throw new ForbiddenException("Only ADMIN or OWNER allowed");
        }
    }

    // =========================
    // COMPANY SCOPE
    // =========================

    public boolean isSameCompany(User user, Long companyId) {
        if (isOwner(user)) return true;
        return user.getCompany() != null && user.getCompany().getId().equals(companyId);
    }

    public boolean isSameCompany(CurrentUser user, Long companyId) {
        if (isOwner(user)) return true;
        return user.getCompanyId() != null && user.getCompanyId().equals(companyId);
    }

    public void requireSameCompany(User user, Long companyId) {
        if (!isSameCompany(user, companyId)) {
            throw new ForbiddenException("Different company access denied");
        }
    }

    public void requireSameCompany(CurrentUser user, Long companyId) {
        if (!isSameCompany(user, companyId)) {
            throw new ForbiddenException("Different company access denied");
        }
    }

    // =========================
    // SUPPLIER ACCESS - BASE (solo lógica, sin dependencias externas)
    // =========================

    public boolean hasSupplierAccessByRole(User user, Supplier supplier) {
        if (isOwner(user)) return true;
        if (isAdmin(user)) {
            return isSameCompany(user, supplier.getCompany().getId());
        }
        if (isUser(user)) {
            return isSameCompany(user, supplier.getCompany().getId()) &&
                    user.getPerson() != null &&
                    user.getPerson().getId().equals(supplier.getPerson().getId());
        }
        return false;
    }

    public boolean hasSupplierAccessByRole(CurrentUser user, Supplier supplier) {
        if (isOwner(user)) return true;
        if (isAdmin(user)) {
            return isSameCompany(user, supplier.getCompany().getId());
        }
        if (isUser(user)) {
            return isSameCompany(user, supplier.getCompany().getId()) &&
                    user.getPersonId() != null &&
                    user.getPersonId().equals(supplier.getPerson().getId());
        }
        return false;
    }

    public void requireSupplierAccessByRole(User user, Supplier supplier) {
        if (!hasSupplierAccessByRole(user, supplier)) {
            throw new ForbiddenException("Access denied to this supplier");
        }
    }

    public void requireSupplierAccessByRole(CurrentUser user, Supplier supplier) {
        if (!hasSupplierAccessByRole(user, supplier)) {
            throw new ForbiddenException("Access denied to this supplier");
        }
    }
}