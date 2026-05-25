package com.timora.app.repository;

import com.timora.app.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByRuc(String ruc);
    Optional<Company> findByEmail(String email);
    boolean existsByRuc(String ruc);
    boolean existsByEmail(String email);
}
