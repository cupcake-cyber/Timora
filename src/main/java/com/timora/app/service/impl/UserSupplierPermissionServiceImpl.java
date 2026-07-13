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
import com.timora.app.security.AccessControlBaseService;
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
    private final AccessControlBaseService accessBase;

    @Override
    @Transactional
    public UserSupplierPermissionDTO create(UserSupplierPermissionCreateDTO dto) {

        System.out.println("🚀 ========== CREATE PERMISSION - INICIO ==========");
        System.out.println("📦 DTO recibido:");
        System.out.println("  - userId: " + dto.getUserId());
        System.out.println("  - supplierId: " + dto.getSupplierId());
        System.out.println("  - permission: " + dto.getPermission());

        try {
            System.out.println("🔍 Obteniendo CurrentUser...");
            CurrentUser currentUser = securityHelper.getCurrentUser();
            System.out.println("👤 CurrentUser:");
            System.out.println("  - userId: " + currentUser.getUserId());
            System.out.println("  - role: " + currentUser.getRole());
            System.out.println("  - companyId: " + currentUser.getCompanyId());

            System.out.println("🔍 Verificando permisos del usuario...");
            if (!accessBase.isOwner(currentUser) && !accessBase.isAdmin(currentUser)) {
                System.out.println("⛔ Usuario NO autorizado: " + currentUser.getRole());
                throw new ForbiddenException("You are not allowed to assign permissions");
            }
            System.out.println("✅ Usuario autorizado");

            System.out.println("🔍 Buscando User con ID: " + dto.getUserId());
            User user = userService.findById(dto.getUserId());
            System.out.println("✅ User encontrado:");
            System.out.println("  - id: " + user.getId());
            System.out.println("  - companyId: " + user.getCompany().getId());
            System.out.println("  - email: " + user.getEmail());

            System.out.println("🔍 Buscando Supplier con ID: " + dto.getSupplierId());
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> {
                        System.out.println("❌ Supplier NO encontrado con ID: " + dto.getSupplierId());
                        return new BusinessException("Supplier not found");
                    });
            System.out.println("✅ Supplier encontrado:");
            System.out.println("  - id: " + supplier.getId());
            System.out.println("  - companyId: " + supplier.getCompany().getId());
            System.out.println("  - specialty: " + supplier.getSpecialty());

            System.out.println("🔍 Validando misma compañía:");
            System.out.println("  - userCompanyId: " + user.getCompany().getId());
            System.out.println("  - supplierCompanyId: " + supplier.getCompany().getId());

            if (!user.getCompany().getId().equals(supplier.getCompany().getId())) {
                System.out.println("❌ Compañías DIFERENTES! User: " + user.getCompany().getId() +
                        ", Supplier: " + supplier.getCompany().getId());
                throw new BusinessException("User and Supplier must belong to the same company");
            }
            System.out.println("✅ Misma compañía validada");

            System.out.println("🔍 Creando ID compuesto:");
            System.out.println("  - userId: " + user.getId());
            System.out.println("  - supplierId: " + supplier.getId());
            System.out.println("  - permission: " + dto.getPermission());

            UserSupplierPermissionId id = new UserSupplierPermissionId(
                    user.getId(),
                    supplier.getId(),
                    dto.getPermission()
            );
            System.out.println("✅ ID compuesto creado");

            System.out.println("🔍 Verificando si el permiso ya existe...");
            boolean exists = userSupplierPermissionRepository.existsById(id);
            System.out.println("  - exists: " + exists);

            if (exists) {
                System.out.println("⚠️ El permiso YA EXISTE: " + id);
                throw new BusinessException("Permission already exists");
            }
            System.out.println("✅ El permiso NO existe, procediendo a crear");

            System.out.println("🔍 Creando entidad UserSupplierPermission...");
            UserSupplierPermission entity = new UserSupplierPermission();
            entity.setId(id);
            entity.setUser(user);
            entity.setSupplier(supplier);

            System.out.println("🔍 Buscando assignedBy (currentUser): " + currentUser.getUserId());
            User assignedBy = userService.findById(currentUser.getUserId());
            entity.setAssignedBy(assignedBy);
            System.out.println("✅ Entidad creada, assignedBy: " + assignedBy.getId());

            System.out.println("💾 Guardando en base de datos...");
            userSupplierPermissionRepository.save(entity);
            System.out.println("✅ Guardado exitoso en BD");

            UserSupplierPermissionDTO result = toDTO(entity);
            System.out.println("✅ Operación completada. Resultado:");
            System.out.println("  - userId: " + result.getUserId());
            System.out.println("  - supplierId: " + result.getSupplierId());
            System.out.println("  - permission: " + result.getPermission());
            System.out.println("🚀 ========== CREATE PERMISSION - FIN ==========");

            return result;

        } catch (Exception e) {
            System.out.println("❌❌❌ ERROR EN CREATE():");
            System.out.println("  - Mensaje: " + e.getMessage());
            System.out.println("  - Clase: " + e.getClass().getName());
            System.out.println("  - Stack trace:");
            e.printStackTrace(System.out);
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(UserSupplierPermissionCreateDTO dto) {

        System.out.println("🗑️ ========== DELETE PERMISSION - INICIO ==========");
        System.out.println("📦 DTO recibido:");
        System.out.println("  - userId: " + dto.getUserId());
        System.out.println("  - supplierId: " + dto.getSupplierId());
        System.out.println("  - permission: " + dto.getPermission());

        try {
            System.out.println("🔍 Obteniendo CurrentUser...");
            CurrentUser currentUser = securityHelper.getCurrentUser();
            System.out.println("👤 CurrentUser:");
            System.out.println("  - userId: " + currentUser.getUserId());
            System.out.println("  - role: " + currentUser.getRole());

            System.out.println("🔍 Verificando permisos de eliminación...");
            if (!accessBase.isOwner(currentUser) && !accessBase.isAdmin(currentUser)) {
                System.out.println("⛔ Usuario NO autorizado: " + currentUser.getRole());
                throw new ForbiddenException("You are not allowed to delete permissions");
            }
            System.out.println("✅ Usuario autorizado");

            System.out.println("🔍 Buscando User con ID: " + dto.getUserId());
            User user = userService.findById(dto.getUserId());
            System.out.println("✅ User encontrado: id=" + user.getId());

            System.out.println("🔍 Buscando Supplier con ID: " + dto.getSupplierId());
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> {
                        System.out.println("❌ Supplier NO encontrado con ID: " + dto.getSupplierId());
                        return new BusinessException("Supplier not found");
                    });
            System.out.println("✅ Supplier encontrado: id=" + supplier.getId());

            if (accessBase.isAdmin(currentUser)) {
                Long companyId = currentUser.getCompanyId();
                System.out.println("🔍 Validando compañía para ADMIN: companyId=" + companyId);

                if (!user.getCompany().getId().equals(companyId)
                        || !supplier.getCompany().getId().equals(companyId)) {
                    System.out.println("❌ Cross-company access denied!");
                    throw new ForbiddenException("Cross-company access denied");
                }
                System.out.println("✅ Compañía validada");
            }

            UserSupplierPermissionId id = new UserSupplierPermissionId(
                    dto.getUserId(),
                    dto.getSupplierId(),
                    dto.getPermission()
            );
            System.out.println("🔍 Verificando si el permiso existe: " + id);

            if (!userSupplierPermissionRepository.existsById(id)) {
                System.out.println("⚠️ El permiso NO EXISTE: " + id);
                throw new BusinessException("Permission does not exist");
            }

            System.out.println("🗑️ Eliminando permiso...");
            userSupplierPermissionRepository.deleteById(id);
            System.out.println("✅ Permiso eliminado exitosamente");
            System.out.println("🗑️ ========== DELETE PERMISSION - FIN ==========");

        } catch (Exception e) {
            System.out.println("❌❌❌ ERROR EN DELETE():");
            System.out.println("  - Mensaje: " + e.getMessage());
            System.out.println("  - Clase: " + e.getClass().getName());
            System.out.println("  - Stack trace:");
            e.printStackTrace(System.out);
            throw e;
        }
    }

    @Override
    public List<UserSupplierPermissionDTO> getByUserId(Long userId) {

        System.out.println("🔍 ========== GET BY USER ID ==========");
        System.out.println("📦 userId: " + userId);

        CurrentUser currentUser = securityHelper.getCurrentUser();
        System.out.println("👤 CurrentUser: " + currentUser.getUserId() + ", role: " + currentUser.getRole());

        if (!accessBase.isOwner(currentUser) && !accessBase.isAdmin(currentUser)) {
            System.out.println("⛔ Usuario no autorizado");
            throw new ForbiddenException("You are not allowed to view permissions");
        }

        User targetUser = userService.findById(userId);
        System.out.println("✅ TargetUser encontrado: id=" + targetUser.getId() + ", companyId=" + targetUser.getCompany().getId());

        if (accessBase.isAdmin(currentUser)) {
            Long companyId = currentUser.getCompanyId();
            System.out.println("🔍 Validando compañía para ADMIN: " + companyId);

            if (!targetUser.getCompany().getId().equals(companyId)) {
                System.out.println("❌ Cross-company access denied");
                throw new ForbiddenException("Cross-company access denied");
            }
            System.out.println("✅ Compañía validada");
        }

        List<UserSupplierPermissionDTO> result = userSupplierPermissionRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .toList();

        System.out.println("📊 Resultados encontrados: " + result.size());
        System.out.println("🔍 ========== FIN GET BY USER ID ==========");

        return result;
    }

    @Override
    public List<UserSupplierPermissionDTO> getBySupplierId(Long supplierId) {

        System.out.println("🔍 ========== GET BY SUPPLIER ID ==========");
        System.out.println("📦 supplierId: " + supplierId);

        CurrentUser currentUser = securityHelper.getCurrentUser();
        System.out.println("👤 CurrentUser: " + currentUser.getUserId() + ", role: " + currentUser.getRole());

        if (!accessBase.isOwner(currentUser) && !accessBase.isAdmin(currentUser)) {
            System.out.println("⛔ Usuario no autorizado");
            throw new ForbiddenException("You are not allowed to view permissions");
        }

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> {
                    System.out.println("❌ Supplier no encontrado con ID: " + supplierId);
                    return new BusinessException("Supplier not found");
                });
        System.out.println("✅ Supplier encontrado: id=" + supplier.getId() + ", companyId=" + supplier.getCompany().getId());

        if (accessBase.isAdmin(currentUser)) {
            Long companyId = currentUser.getCompanyId();
            System.out.println("🔍 Validando compañía para ADMIN: " + companyId);

            if (!supplier.getCompany().getId().equals(companyId)) {
                System.out.println("❌ Cross-company access denied");
                throw new ForbiddenException("Cross-company access denied");
            }
            System.out.println("✅ Compañía validada");
        }

        List<UserSupplierPermissionDTO> result = userSupplierPermissionRepository.findBySupplierId(supplierId)
                .stream()
                .map(this::toDTO)
                .toList();

        System.out.println("📊 Resultados encontrados: " + result.size());
        System.out.println("🔍 ========== FIN GET BY SUPPLIER ID ==========");

        return result;
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

        System.out.println("🗺️ ========== GET PERMISSION MAP ==========");
        System.out.println("📦 userId: " + userId);

        CurrentUser currentUser = securityHelper.getCurrentUser();
        System.out.println("👤 CurrentUser: " + currentUser.getUserId() + ", role: " + currentUser.getRole());

        // 🔥 PERMITIR QUE EL USUARIO VEA SUS PROPIOS PERMISOS
        boolean isOwner = accessBase.isOwner(currentUser);
        boolean isAdmin = accessBase.isAdmin(currentUser);
        boolean isSelf = currentUser.getUserId().equals(userId);

        System.out.println("🔍 Verificando permisos:");
        System.out.println("  - isOwner: " + isOwner);
        System.out.println("  - isAdmin: " + isAdmin);
        System.out.println("  - isSelf: " + isSelf);

        if (!isOwner && !isAdmin && !isSelf) {
            System.out.println("⛔ Usuario no autorizado");
            throw new ForbiddenException("You are not allowed to view permissions");
        }
        System.out.println("✅ Usuario autorizado");

        // Si es USER viendo sus propios permisos, no necesita validación de compañía
        if (isSelf && !isOwner && !isAdmin) {
            System.out.println("👤 USER viendo sus propios permisos - acceso permitido");
            // Solo buscar sus permisos
            List<UserSupplierPermission> permissions = userSupplierPermissionRepository.findByUserId(userId);
            Map<Long, Set<Permission>> map = new HashMap<>();
            for (UserSupplierPermission permission : permissions) {
                map.computeIfAbsent(
                        permission.getSupplier().getId(),
                        k -> new HashSet<>()
                ).add(permission.getId().getPermission());
            }
            return new UserPermissionMapDTO(map);
        }

        // Para OWNER y ADMIN, validar que el usuario existe
        User targetUser = userService.findById(userId);
        System.out.println("✅ TargetUser encontrado: id=" + targetUser.getId() + ", companyId=" + targetUser.getCompany().getId());

        if (isAdmin) {
            Long companyId = currentUser.getCompanyId();
            System.out.println("🔍 Validando compañía para ADMIN: " + companyId);

            if (!targetUser.getCompany().getId().equals(companyId)) {
                System.out.println("❌ Cross-company access denied");
                throw new ForbiddenException("Cross-company access denied");
            }
            System.out.println("✅ Compañía validada");
        }

        System.out.println("🔍 Buscando permisos para userId: " + userId);
        List<UserSupplierPermission> permissions = userSupplierPermissionRepository.findByUserId(userId);
        System.out.println("📊 Permisos encontrados: " + permissions.size());

        Map<Long, Set<Permission>> map = new HashMap<>();

        for (UserSupplierPermission permission : permissions) {
            Long supplierId = permission.getSupplier().getId();
            Permission perm = permission.getId().getPermission();

            System.out.println("  - supplierId: " + supplierId + ", permission: " + perm);

            map.computeIfAbsent(
                    supplierId,
                    k -> new HashSet<>()
            ).add(perm);
        }

        System.out.println("📊 Mapa resultante:");
        for (Map.Entry<Long, Set<Permission>> entry : map.entrySet()) {
            System.out.println("  - supplierId: " + entry.getKey() + ", permissions: " + entry.getValue());
        }

        System.out.println("🗺️ ========== FIN GET PERMISSION MAP ==========");

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

        System.out.println("🔍 hasAnyPermission: userId=" + resolvedUserId);
        boolean result = userSupplierPermissionRepository.existsByUser_Id(resolvedUserId);
        System.out.println("  - result: " + result);

        return result;
    }

    @Override
    public boolean hasPermission(Long userId, Long supplierId, Permission permission) {
        Long resolvedUserId = (userId != null) ? userId : securityHelper.getCurrentUser().getUserId();

        System.out.println("🔍 hasPermission:");
        System.out.println("  - userId: " + resolvedUserId);
        System.out.println("  - supplierId: " + supplierId);
        System.out.println("  - permission: " + permission);

        if (supplierId == null) {
            List<UserSupplierPermission> permissions = userSupplierPermissionRepository.findByUserId(resolvedUserId);
            boolean result = permissions.stream()
                    .anyMatch(p -> p.getId().getPermission() == permission);
            System.out.println("  - result (any supplier): " + result);
            return result;
        }

        boolean result = userSupplierPermissionRepository.existsByUser_IdAndSupplier_IdAndId_Permission(
                resolvedUserId, supplierId, permission
        );
        System.out.println("  - result (specific): " + result);

        return result;
    }

    @Override
    public boolean hasAnyPermissionOnSupplier(Long userId, Long supplierId) {
        Long resolvedUserId = (userId != null) ? userId : securityHelper.getCurrentUser().getUserId();

        System.out.println("🔍 hasAnyPermissionOnSupplier:");
        System.out.println("  - userId: " + resolvedUserId);
        System.out.println("  - supplierId: " + supplierId);

        boolean result = userSupplierPermissionRepository.existsByUser_IdAndSupplier_Id(resolvedUserId, supplierId);
        System.out.println("  - result: " + result);

        return result;
    }

    @Override
    public Set<Permission> getPermissionsForSupplier(Long userId, Long supplierId) {
        Long resolvedUserId = (userId != null) ? userId : securityHelper.getCurrentUser().getUserId();

        System.out.println("🔍 getPermissionsForSupplier:");
        System.out.println("  - userId: " + resolvedUserId);
        System.out.println("  - supplierId: " + supplierId);

        Set<Permission> result = userSupplierPermissionRepository.findPermissionsByUser_IdAndSupplier_Id(resolvedUserId, supplierId);
        System.out.println("  - permissions: " + result);

        return result;
    }

    @Override
    public boolean hasAnyPermission(Long userId, Permission permission) {
        Long resolvedUserId = (userId != null) ? userId : securityHelper.getCurrentUser().getUserId();

        System.out.println("🔍 hasAnyPermission (by permission):");
        System.out.println("  - userId: " + resolvedUserId);
        System.out.println("  - permission: " + permission);

        boolean result = userSupplierPermissionRepository.existsByUser_IdAndId_Permission(resolvedUserId, permission);
        System.out.println("  - result: " + result);

        return result;
    }
}