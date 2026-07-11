package com.timora.app.security;

import com.timora.app.dto.security.CurrentUser;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.model.Supplier;
import com.timora.app.model.User;
import com.timora.app.model.enums.Permission;
import com.timora.app.service.UserSupplierPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessControlService {

    private final AccessControlBaseService baseService;
    private final UserSupplierPermissionService permissionService;

    // =========================
    // SUPPLIER ACCESS - COMPLETE
    // =========================

    public boolean hasAccessToSupplier(User user, Supplier supplier) {
        if (baseService.isOwner(user)) return true;
        if (baseService.isAdmin(user)) {
            return baseService.isSameCompany(user, supplier.getCompany().getId());
        }
        if (baseService.isUser(user)) {
            return baseService.isSameCompany(user, supplier.getCompany().getId()) &&
                    user.getPerson() != null &&
                    user.getPerson().getId().equals(supplier.getPerson().getId());
        }
        return false;
    }

    public boolean hasAccessToSupplier(CurrentUser user, Supplier supplier) {
        if (baseService.isOwner(user)) return true;
        if (baseService.isAdmin(user)) {
            return baseService.isSameCompany(user, supplier.getCompany().getId());
        }
        if (baseService.isUser(user)) {
            return baseService.isSameCompany(user, supplier.getCompany().getId()) &&
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
        if (baseService.isOwner(user)) return true;
        if (baseService.isAdmin(user)) {
            return baseService.isSameCompany(user, supplier.getCompany().getId());
        }
        if (baseService.isUser(user)) {
            return permissionService.hasPermission(user.getId(), supplier.getId(), permission);
        }
        return false;
    }

    public boolean hasPermission(CurrentUser user, Supplier supplier, Permission permission) {
        if (baseService.isOwner(user)) return true;
        if (baseService.isAdmin(user)) {
            return baseService.isSameCompany(user, supplier.getCompany().getId());
        }
        if (baseService.isUser(user)) {
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
    // PERMISSION CHECKS - ANYWHERE
    // =========================

    public boolean hasPermissionAnywhere(User user, Permission permission) {
        if (baseService.isOwner(user)) return true;
        if (baseService.isAdmin(user)) return true;
        if (baseService.isUser(user)) {
            return permissionService.hasAnyPermission(user.getId(), permission);
        }
        return false;
    }

    public boolean hasPermissionAnywhere(CurrentUser user, Permission permission) {
        if (baseService.isOwner(user)) return true;
        if (baseService.isAdmin(user)) return true;
        if (baseService.isUser(user)) {
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
    // DELEGATED METHODS - BOOKING
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
    // DELEGATED METHODS - SERVICE
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
    // DELEGATED METHODS - CUSTOMER (con supplier específico)
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
    // CUSTOMER ANYWHERE
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
    // AVAILABILITY
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