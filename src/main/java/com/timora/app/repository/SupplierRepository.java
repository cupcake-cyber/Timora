package com.timora.app.repository;

import com.timora.app.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    boolean existsByPersonId(Long personId);

    Optional<Supplier> findByPersonId(Long personId);

    void deleteByPersonId(Long personId);
}