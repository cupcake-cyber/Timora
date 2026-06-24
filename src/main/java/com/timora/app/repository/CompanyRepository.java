package com.timora.app.repository;

import com.timora.app.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByRuc(String ruc);

    boolean existsByEmail(String email);

    @Query("SELECT c FROM Company c WHERE c.status = 'ACTIVE'")
    List<Company> findAll();

    @Query("SELECT c FROM Company c WHERE c.id = :id AND c.status = 'ACTIVE'")
    Optional<Company> findById(Long id);
}