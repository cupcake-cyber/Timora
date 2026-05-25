package com.timora.app.repository;

import com.timora.app.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
    boolean existsByName(String name);
    List<Role> findByCompanyId(Long companyId);
}
