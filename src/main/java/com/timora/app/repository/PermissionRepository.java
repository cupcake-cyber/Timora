package com.timora.app.repository;

import com.timora.app.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
    Optional<Permission> findByCode(String code);

    boolean existsByCode(String code);
}
