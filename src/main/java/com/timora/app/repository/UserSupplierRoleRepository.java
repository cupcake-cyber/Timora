package com.timora.app.repository;

import com.timora.app.models.UserSupplierRole;
import com.timora.app.models.UserSupplierRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSupplierRoleRepository
        extends JpaRepository<UserSupplierRole, UserSupplierRoleId> {

    List<UserSupplierRole> findByUserId(Long userId);

    List<UserSupplierRole> findBySupplierId(Long supplierId);

    List<UserSupplierRole> findByRoleId(Long roleId);

    Optional<UserSupplierRole> findByUserIdAndSupplierIdAndRoleId(
            Long userId,
            Long supplierId,
            Long roleId
    );
}