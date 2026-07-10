package com.timora.app.security;

import com.timora.app.dto.security.CurrentUser;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.model.Supplier;
import com.timora.app.model.User;
import com.timora.app.model.enums.GlobalRole;
import com.timora.app.model.enums.Permission;
import com.timora.app.service.UserSupplierPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessControlService {

    private final UserSupplierPermissionService permissionService;

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
    // SUPPLIER ACCESS - BASE
    // =========================

    public boolean hasAccessToSupplier(User user, Supplier supplier) {
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
    public boolean hasAccessToSupplier(CurrentUser user, Supplier supplier) {
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
    public void requireSupplierAccess(User user, Supplier supplier) {
        if (!hasAccessToSupplier(user, supplier)) {
            throw new ForbiddenException("Access denied to this supplier");
        }
    }
    public void requireSupplierAccess(CurrentUser user, Supplier supplier) {
        if (!hasAccessToSupplier(user, supplier)) {
            throw new ForbiddenException("Access denied to this supplier");
        }
    }

    // =========================
    // PERMISSION CHECKS (con supplier específico)
    // =========================

    public boolean hasPermission(User user, Supplier supplier, Permission permission) {
        if (isOwner(user)) return true;
        if (isAdmin(user)) {
            return isSameCompany(user, supplier.getCompany().getId());
        }
        if (isUser(user)) {
            return permissionService.hasPermission(user.getId(), supplier.getId(), permission);
        }
        return false;
    }
    public boolean hasPermission(CurrentUser user, Supplier supplier, Permission permission) {
        if (isOwner(user)) return true;
        if (isAdmin(user)) {
            return isSameCompany(user, supplier.getCompany().getId());
        }
        if (isUser(user)) {
            return permissionService.hasPermission(user.getUserId(), supplier.getId(), permission);
        }
        return false;
    }

    public void requirePermission(User user, Supplier supplier, Permission permission) {
        if (!hasPermission(user, supplier, permission)) {
            throw new ForbiddenException(
                    String.format("Permission '%s' required for this supplier", permission)
            );
        }
    }
    public void requirePermission(CurrentUser user, Supplier supplier, Permission permission) {
        if (!hasPermission(user, supplier, permission)) {
            throw new ForbiddenException(
                    String.format("Permission '%s' required for this supplier", permission)
            );
        }
    }

    // =========================
    // PERMISSION CHECKS - ANYWHERE (sin supplier específico) 🔥 NUEVO
    // =========================

    public boolean hasPermissionAnywhere(User user, Permission permission) {
        if (isOwner(user)) return true;
        if (isAdmin(user)) return true;
        if (isUser(user)) {
            return permissionService.hasAnyPermission(user.getId(), permission);
        }
        return false;
    }

    public boolean hasPermissionAnywhere(CurrentUser user, Permission permission) {
        if (isOwner(user)) return true;
        if (isAdmin(user)) return true;
        if (isUser(user)) {
            return permissionService.hasAnyPermission(user.getUserId(), permission);
        }
        return false;
    }

    public void requirePermissionAnywhere(User user, Permission permission) {
        if (!hasPermissionAnywhere(user, permission)) {
            throw new ForbiddenException(
                    String.format("You don't have permission '%s' from any supplier", permission)
            );
        }
    }

    public void requirePermissionAnywhere(CurrentUser user, Permission permission) {
        if (!hasPermissionAnywhere(user, permission)) {
            throw new ForbiddenException(
                    String.format("You don't have permission '%s' from any supplier", permission)
            );
        }
    }

    // =========================
    // PERMISSION CHECKS CON OVERLOAD - BOOKING
    // =========================

    public boolean canCreateBooking(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.BOOKING_CREATE);
    }
    public boolean canCreateBooking(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.BOOKING_CREATE);
    }
    public void requireCanCreateBooking(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.BOOKING_CREATE);
    }
    public void requireCanCreateBooking(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.BOOKING_CREATE);
    }
    public boolean canReadBooking(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.BOOKING_READ);
    }
    public boolean canReadBooking(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.BOOKING_READ);
    }
    public void requireCanReadBooking(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.BOOKING_READ);
    }
    public void requireCanReadBooking(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.BOOKING_READ);
    }
    public boolean canUpdateBooking(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.BOOKING_UPDATE);
    }
    public boolean canUpdateBooking(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.BOOKING_UPDATE);
    }
    public void requireCanUpdateBooking(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.BOOKING_UPDATE);
    }
    public void requireCanUpdateBooking(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.BOOKING_UPDATE);
    }
    public boolean canDeleteBooking(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.BOOKING_DELETE);
    }
    public boolean canDeleteBooking(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.BOOKING_DELETE);
    }
    public void requireCanDeleteBooking(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.BOOKING_DELETE);
    }
    public void requireCanDeleteBooking(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.BOOKING_DELETE);
    }

    // =========================
    // PERMISSION CHECKS CON OVERLOAD - SERVICE
    // =========================

    public boolean canCreateService(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.SERVICE_CREATE);
    }
    public boolean canCreateService(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.SERVICE_CREATE);
    }
    public void requireCanCreateService(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.SERVICE_CREATE);
    }
    public void requireCanCreateService(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.SERVICE_CREATE);
    }
    public boolean canReadService(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.SERVICE_READ);
    }
    public boolean canReadService(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.SERVICE_READ);
    }
    public void requireCanReadService(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.SERVICE_READ);
    }
    public void requireCanReadService(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.SERVICE_READ);
    }
    public boolean canUpdateService(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.SERVICE_UPDATE);
    }
    public boolean canUpdateService(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.SERVICE_UPDATE);
    }
    public void requireCanUpdateService(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.SERVICE_UPDATE);
    }
    public void requireCanUpdateService(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.SERVICE_UPDATE);
    }
    public boolean canDeleteService(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.SERVICE_DELETE);
    }
    public boolean canDeleteService(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.SERVICE_DELETE);
    }
    public void requireCanDeleteService(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.SERVICE_DELETE);
    }
    public void requireCanDeleteService(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.SERVICE_DELETE);
    }

    // =========================
    // PERMISSION CHECKS CON OVERLOAD - CUSTOMER (con supplier específico)
    // =========================

    public boolean canCreateCustomer(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.CUSTOMER_CREATE);
    }
    public boolean canCreateCustomer(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.CUSTOMER_CREATE);
    }
    public void requireCanCreateCustomer(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.CUSTOMER_CREATE);
    }
    public void requireCanCreateCustomer(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.CUSTOMER_CREATE);
    }

    public boolean canReadCustomer(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.CUSTOMER_READ);
    }
    public boolean canReadCustomer(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.CUSTOMER_READ);
    }
    public void requireCanReadCustomer(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.CUSTOMER_READ);
    }
    public void requireCanReadCustomer(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.CUSTOMER_READ);
    }

    public boolean canUpdateCustomer(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.CUSTOMER_UPDATE);
    }
    public boolean canUpdateCustomer(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.CUSTOMER_UPDATE);
    }
    public void requireCanUpdateCustomer(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.CUSTOMER_UPDATE);
    }
    public void requireCanUpdateCustomer(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.CUSTOMER_UPDATE);
    }

    public boolean canDeleteCustomer(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.CUSTOMER_DELETE);
    }
    public boolean canDeleteCustomer(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.CUSTOMER_DELETE);
    }
    public void requireCanDeleteCustomer(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.CUSTOMER_DELETE);
    }
    public void requireCanDeleteCustomer(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.CUSTOMER_DELETE);
    }

    // =========================
    // PERMISSION CHECKS - CUSTOMER ANYWHERE (sin supplier específico) 🔥 NUEVO
    // =========================

    public boolean canCreateCustomerAnywhere(User user) {
        return hasPermissionAnywhere(user, Permission.CUSTOMER_CREATE);
    }
    public boolean canCreateCustomerAnywhere(CurrentUser user) {
        return hasPermissionAnywhere(user, Permission.CUSTOMER_CREATE);
    }
    public void requireCanCreateCustomerAnywhere(User user) {
        requirePermissionAnywhere(user, Permission.CUSTOMER_CREATE);
    }
    public void requireCanCreateCustomerAnywhere(CurrentUser user) {
        requirePermissionAnywhere(user, Permission.CUSTOMER_CREATE);
    }

    public boolean canUpdateCustomerAnywhere(User user) {
        return hasPermissionAnywhere(user, Permission.CUSTOMER_UPDATE);
    }
    public boolean canUpdateCustomerAnywhere(CurrentUser user) {
        return hasPermissionAnywhere(user, Permission.CUSTOMER_UPDATE);
    }
    public void requireCanUpdateCustomerAnywhere(User user) {
        requirePermissionAnywhere(user, Permission.CUSTOMER_UPDATE);
    }
    public void requireCanUpdateCustomerAnywhere(CurrentUser user) {
        requirePermissionAnywhere(user, Permission.CUSTOMER_UPDATE);
    }

    public boolean canReadCustomerAnywhere(User user) {
        return hasPermissionAnywhere(user, Permission.CUSTOMER_READ);
    }
    public boolean canReadCustomerAnywhere(CurrentUser user) {
        return hasPermissionAnywhere(user, Permission.CUSTOMER_READ);
    }
    public void requireCanReadCustomerAnywhere(User user) {
        requirePermissionAnywhere(user, Permission.CUSTOMER_READ);
    }
    public void requireCanReadCustomerAnywhere(CurrentUser user) {
        requirePermissionAnywhere(user, Permission.CUSTOMER_READ);
    }

    public boolean canDeleteCustomerAnywhere(User user) {
        return hasPermissionAnywhere(user, Permission.CUSTOMER_DELETE);
    }
    public boolean canDeleteCustomerAnywhere(CurrentUser user) {
        return hasPermissionAnywhere(user, Permission.CUSTOMER_DELETE);
    }
    public void requireCanDeleteCustomerAnywhere(User user) {
        requirePermissionAnywhere(user, Permission.CUSTOMER_DELETE);
    }
    public void requireCanDeleteCustomerAnywhere(CurrentUser user) {
        requirePermissionAnywhere(user, Permission.CUSTOMER_DELETE);
    }

    // =========================
    // PERMISSION CHECKS CON OVERLOAD - ABILITY
    // =========================

    public boolean canCreateAvailability(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.AVAILABILITY_CREATE);
    }
    public boolean canCreateAvailability(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.AVAILABILITY_CREATE);
    }
    public void requireCanCreateAvailability(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.AVAILABILITY_CREATE);
    }
    public void requireCanCreateAvailability(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.AVAILABILITY_CREATE);
    }

    public boolean canReadAvailability(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.AVAILABILITY_READ);
    }
    public boolean canReadAvailability(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.AVAILABILITY_READ);
    }
    public void requireCanReadAvailability(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.AVAILABILITY_READ);
    }
    public void requireCanReadAvailability(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.AVAILABILITY_READ);
    }

    public boolean canUpdateAvailability(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.AVAILABILITY_UPDATE);
    }
    public boolean canUpdateAvailability(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.AVAILABILITY_UPDATE);
    }
    public void requireCanUpdateAvailability(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.AVAILABILITY_UPDATE);
    }
    public void requireCanUpdateAvailability(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.AVAILABILITY_UPDATE);
    }

    public boolean canDeleteAvailability(User user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.AVAILABILITY_DELETE);
    }
    public boolean canDeleteAvailability(CurrentUser user, Supplier supplier) {
        return hasPermission(user, supplier, Permission.AVAILABILITY_DELETE);
    }
    public void requireCanDeleteAvailability(User user, Supplier supplier) {
        requirePermission(user, supplier, Permission.AVAILABILITY_DELETE);
    }
    public void requireCanDeleteAvailability(CurrentUser user, Supplier supplier) {
        requirePermission(user, supplier, Permission.AVAILABILITY_DELETE);
    }
}