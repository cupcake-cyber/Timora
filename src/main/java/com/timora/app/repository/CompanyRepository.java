package com.timora.app.repository;

import com.timora.app.model.Company;
import com.timora.app.model.enums.CompanyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByRuc(String ruc);
    boolean existsByEmail(String email);

    List<Company> findByStatus(CompanyStatus status);
    Optional<Company> findByIdAndStatus(Long id, CompanyStatus status);
}