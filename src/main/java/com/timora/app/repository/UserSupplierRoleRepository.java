package com.timora.app.repository;

import com.timora.app.model.UserSupplierRole;
import com.timora.app.model.UserSupplierRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserSupplierRoleRepository
        extends JpaRepository<UserSupplierRole, UserSupplierRoleId> {

    List<UserSupplierRole> findByUser_Id(Long userId);

    List<UserSupplierRole> findBySupplier_Id(Long supplierId);

    List<UserSupplierRole> findByRole_Id(Long roleId);

    Optional<UserSupplierRole> findByUser_IdAndSupplier_IdAndRole_Id(
            Long userId,
            Long supplierId,
            Long roleId
    );
        @Query("""
        SELECT usr.supplier.id
        FROM UserSupplierRole usr
        WHERE usr.user.id = :userId
    """)
        List<Long> findSupplierIdsByUserId(@Param("userId") Long userId);
}