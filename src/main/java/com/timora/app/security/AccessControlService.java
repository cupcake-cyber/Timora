package com.timora.app.security;

import com.timora.app.exception.ForbiddenException;
import com.timora.app.model.Supplier;
import com.timora.app.model.User;
import com.timora.app.model.enums.GlobalRole;
import org.springframework.stereotype.Component;

@Component
public class AccessControlService {

    // =========================
    // ROLES BASE
    // =========================

    public void requireOwner(User user) {
        if (user.getRole() != GlobalRole.OWNER) {
            throw new ForbiddenException("Only OWNER allowed");
        }
    }

    public void requireAdminOrOwner(User user) {
        if (user.getRole() == GlobalRole.USER) {
            throw new ForbiddenException("Insufficient permissions");
        }
    }

    public boolean isOwner(User user) {
        return user.getRole() == GlobalRole.OWNER;
    }

    public boolean isAdmin(User user) {
        return user.getRole() == GlobalRole.ADMIN;
    }

    public boolean isUser(User user) {
        return user.getRole() == GlobalRole.USER;
    }

    // =========================
    // COMPANY SCOPE
    // =========================

    public void requireSameCompany(User user, Long companyId) {

        if (isOwner(user)) return;

        if (user.getCompany() == null ||
                !user.getCompany().getId().equals(companyId)) {
            throw new ForbiddenException("Different company access denied");
        }
    }

    // =========================
    // SUPPLIER ACCESS
    // =========================

    public void requireSupplierAccess(User user, Supplier supplier) {

        if (isOwner(user)) return;

        if (user.getCompany() == null ||
                !user.getCompany().getId().equals(supplier.getCompany().getId())) {
            throw new ForbiddenException("Supplier not in your company");
        }
    }

    // =========================
    // USER → SUPPLIER RULE
    // =========================

    public void requireUserOwnsSupplier(User user, Supplier supplier) {

        if (!isUser(user)) return;

        if (user.getPerson() == null) {
            throw new ForbiddenException("USER not linked to person/supplier");
        }

        // ⚠️ aquí depende tu modelo real
        // si Person == Supplier o relación directa
        if (!user.getPerson().getId().equals(supplier.getId())) {
            throw new ForbiddenException("USER can only use own supplier");
        }
    }

    // =========================
    // SERVICE PERMISSIONS
    // =========================

    public void requireCanCreateService(User user, Supplier supplier) {

        if (isOwner(user)) return;

        if (isAdmin(user)) {
            requireSupplierAccess(user, supplier);
            return;
        }

        if (isUser(user)) {
            requireUserOwnsSupplier(user, supplier);
        }
    }

    public void requireCanModifyService(User user) {

        if (isOwner(user)) return;

        if (isAdmin(user)) return;

        throw new ForbiddenException("USER cannot modify services");
    }

    public void requireCanDeleteService(User user) {

        if (isOwner(user)) return;

        if (isAdmin(user)) return;

        throw new ForbiddenException("USER cannot delete services");
    }
}