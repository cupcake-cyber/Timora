package com.timora.app.service.impl;

import com.timora.app.dto.security.CurrentUser;
import com.timora.app.dto.usersupplierpermission.UserPermissionMapDTO;
import com.timora.app.dto.usersupplierpermission.UserSupplierPermissionCreateDTO;
import com.timora.app.dto.usersupplierpermission.UserSupplierPermissionDTO;
import com.timora.app.exception.BusinessException;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.model.*;
import com.timora.app.model.enums.Permission;
import com.timora.app.repository.SupplierRepository;
import com.timora.app.repository.UserSupplierPermissionRepository;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.UserService;
import com.timora.app.service.UserSupplierPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@AllArgsConstructor
public class UserSupplierPermissionServiceImpl implements UserSupplierPermissionService {

    private final UserSupplierPermissionRepository userSupplierPermissionRepository;
    private final UserService userService;
    private final SupplierRepository supplierRepository;
    private final SecurityHelper securityHelper;
    private final AccessControlService auth;

    @Override
    @Transactional
    public UserSupplierPermissionDTO create(UserSupplierPermissionCreateDTO dto) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        if (!auth.isOwner(currentUser) && !auth.isAdmin(currentUser)) {
            throw new ForbiddenException("You are not allowed to assign permissions");
        }

        User user = userService.findById(dto.getUserId());

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new BusinessException("Supplier not found"));

        // misma empresa (muy importante en tu modelo)
        if (!user.getCompany().getId().equals(supplier.getCompany().getId())) {
            throw new BusinessException("User and Supplier must belong to the same company");
        }

        UserSupplierPermissionId id = new UserSupplierPermissionId(
                user.getId(),
                supplier.getId(),
                dto.getPermission()
        );

        if (userSupplierPermissionRepository.existsById(id)) {
            throw new BusinessException("Permission already exists");
        }

        UserSupplierPermission entity = new UserSupplierPermission();
        entity.setId(id);
        entity.setUser(user);
        entity.setSupplier(supplier);
        entity.setAssignedBy(userService.findById(currentUser.getUserId()));

        userSupplierPermissionRepository.save(entity);

        return toDTO(entity);
    }

    @Override
    @Transactional
    public void delete(UserSupplierPermissionCreateDTO dto) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        if (!auth.isOwner(currentUser) && !auth.isAdmin(currentUser)) {
            throw new ForbiddenException("You are not allowed to delete permissions");
        }

        User user = userService.findById(dto.getUserId());

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new BusinessException("Supplier not found"));

        // tenant
        if (auth.isAdmin(currentUser)) {

            Long companyId = currentUser.getCompanyId();

            if (!user.getCompany().getId().equals(companyId)
                    || !supplier.getCompany().getId().equals(companyId)) {
                throw new ForbiddenException("Cross-company access denied");
            }
        }

        UserSupplierPermissionId id = new UserSupplierPermissionId(
                dto.getUserId(),
                dto.getSupplierId(),
                dto.getPermission()
        );

        if (!userSupplierPermissionRepository.existsById(id)) {
            throw new BusinessException("Permission does not exist");
        }

        userSupplierPermissionRepository.deleteById(id);
    }

    @Override
    public List<UserSupplierPermissionDTO> getByUserId(Long userId) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        if (!auth.isOwner(currentUser) && !auth.isAdmin(currentUser)) {
            throw new ForbiddenException("You are not allowed to view permissions");
        }

        User targetUser = userService.findById(userId);

        // tenant
        if (auth.isAdmin(currentUser)) {

            Long companyId = currentUser.getCompanyId();

            if (!targetUser.getCompany().getId().equals(companyId)) {
                throw new ForbiddenException("Cross-company access denied");
            }
        }

        return userSupplierPermissionRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<UserSupplierPermissionDTO> getBySupplierId(Long supplierId) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        if (!auth.isOwner(currentUser) && !auth.isAdmin(currentUser)) {
            throw new ForbiddenException("You are not allowed to view permissions");
        }

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new BusinessException("Supplier not found"));

        // tenant
        if (auth.isAdmin(currentUser)) {

            Long companyId = currentUser.getCompanyId();

            if (!supplier.getCompany().getId().equals(companyId)) {
                throw new ForbiddenException("Cross-company access denied");
            }
        }

        return userSupplierPermissionRepository.findBySupplierId(supplierId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private UserSupplierPermissionDTO toDTO(UserSupplierPermission entity) {

        return new UserSupplierPermissionDTO(
                entity.getUser().getId(),
                entity.getSupplier().getId(),
                entity.getId().getPermission(),
                entity.getAssignedBy() != null ? entity.getAssignedBy().getId() : null,
                entity.getCreatedAt()
        );
    }
    @Override
    public UserPermissionMapDTO getPermissionMap(Long userId) {

        CurrentUser currentUser = securityHelper.getCurrentUser();

        if (!auth.isOwner(currentUser) && !auth.isAdmin(currentUser)) {
            throw new ForbiddenException("You are not allowed to view permissions");
        }

        User targetUser = userService.findById(userId);

        // Un ADMIN solo puede consultar usuarios de su empresa
        if (auth.isAdmin(currentUser)) {
            Long companyId = currentUser.getCompanyId();

            if (!targetUser.getCompany().getId().equals(companyId)) {
                throw new ForbiddenException("Cross-company access denied");
            }
        }

        List<UserSupplierPermission> permissions =
                userSupplierPermissionRepository.findByUserId(userId);

        Map<Long, Set<Permission>> map = new HashMap<>();

        for (UserSupplierPermission permission : permissions) {

            map.computeIfAbsent(
                    permission.getSupplier().getId(),
                    k -> new HashSet<>()
            ).add(permission.getId().getPermission());
        }

        return new UserPermissionMapDTO(map);
    }
    @Override
    public boolean hasAnyPermission(Long userId) {

        Long resolvedUserId;

        if (userId == null) {
            CurrentUser currentUser = securityHelper.getCurrentUser();
            resolvedUserId = currentUser.getUserId();
        } else {
            resolvedUserId = userId;
        }

        return userSupplierPermissionRepository
                .existsByUser_Id(resolvedUserId);
    }

    @Override
    public boolean hasPermission(Long userId, Long supplierId, Permission permission) {
        // Si userId es null, usar el usuario actual
        Long resolvedUserId = (userId != null) ? userId : securityHelper.getCurrentUser().getUserId();

        // Si supplierId es null, significa que queremos verificar si tiene el permiso en ALGÚN proveedor
        if (supplierId == null) {
            // Obtener todos los permisos del usuario y verificar si tiene ese permiso en algún lado
            List<UserSupplierPermission> permissions = userSupplierPermissionRepository.findByUserId(resolvedUserId);
            return permissions.stream()
                    .anyMatch(p -> p.getId().getPermission() == permission);
        }

        // Verificar permiso específico para un proveedor
        return userSupplierPermissionRepository.existsByUser_IdAndSupplier_IdAndId_Permission(
                resolvedUserId, supplierId, permission
        );
    }

    @Override
    public boolean hasAnyPermissionOnSupplier(Long userId, Long supplierId) {
        Long resolvedUserId = (userId != null) ? userId : securityHelper.getCurrentUser().getUserId();

        return userSupplierPermissionRepository.existsByUser_IdAndSupplier_Id(resolvedUserId, supplierId);
    }

    @Override
    public Set<Permission> getPermissionsForSupplier(Long userId, Long supplierId) {
        Long resolvedUserId = (userId != null) ? userId : securityHelper.getCurrentUser().getUserId();

        return userSupplierPermissionRepository.findPermissionsByUser_IdAndSupplier_Id(resolvedUserId, supplierId);
    }


    @Override
    public boolean hasAnyPermission(Long userId, Permission permission) {
        Long resolvedUserId = (userId != null) ? userId : securityHelper.getCurrentUser().getUserId();
        return userSupplierPermissionRepository.existsByUser_IdAndId_Permission(resolvedUserId, permission);
    }
}