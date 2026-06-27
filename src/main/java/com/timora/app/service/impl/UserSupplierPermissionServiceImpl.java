package com.timora.app.service.impl;

import com.timora.app.dto.usersupplierpermission.UserSupplierPermissionCreateDTO;
import com.timora.app.dto.usersupplierpermission.UserSupplierPermissionDTO;
import com.timora.app.exception.BusinessException;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.model.*;
import com.timora.app.repository.SupplierRepository;
import com.timora.app.repository.UserRepository;
import com.timora.app.repository.UserSupplierPermissionRepository;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.UserSupplierPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserSupplierPermissionServiceImpl implements UserSupplierPermissionService {

    private final UserSupplierPermissionRepository userSupplierPermissionRepository;
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final SecurityHelper securityHelper;
    private final AccessControlService auth;

    @Override
    @Transactional
    public UserSupplierPermissionDTO create(UserSupplierPermissionCreateDTO dto) {

        User currentUser = securityHelper.getCurrentUser();

        if (!auth.isOwner(currentUser) && !auth.isAdmin(currentUser)) {
            throw new ForbiddenException("You are not allowed to assign permissions");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new BusinessException("User not found"));

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
        entity.setAssignedBy(currentUser);

        userSupplierPermissionRepository.save(entity);

        return toDTO(entity);
    }

    @Override
    @Transactional
    public void delete(UserSupplierPermissionCreateDTO dto) {

        User currentUser = securityHelper.getCurrentUser();

        if (!auth.isOwner(currentUser) && !auth.isAdmin(currentUser)) {
            throw new ForbiddenException("You are not allowed to delete permissions");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new BusinessException("User not found"));

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new BusinessException("Supplier not found"));

        // tenant
        if (auth.isAdmin(currentUser)) {

            Long companyId = currentUser.getCompany().getId();

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

        User currentUser = securityHelper.getCurrentUser();

        if (!auth.isOwner(currentUser) && !auth.isAdmin(currentUser)) {
            throw new ForbiddenException("You are not allowed to view permissions");
        }

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));

        // tenant
        if (auth.isAdmin(currentUser)) {

            Long companyId = currentUser.getCompany().getId();

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

        User currentUser = securityHelper.getCurrentUser();

        if (!auth.isOwner(currentUser) && !auth.isAdmin(currentUser)) {
            throw new ForbiddenException("You are not allowed to view permissions");
        }

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new BusinessException("Supplier not found"));

        // tenant
        if (auth.isAdmin(currentUser)) {

            Long companyId = currentUser.getCompany().getId();

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
}