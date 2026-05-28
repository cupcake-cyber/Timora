package com.timora.app.repository;

import com.timora.app.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByIdAndCompanyId(Long id, Long companyId);

    @Query("""
        SELECT s 
        FROM Supplier s 
        WHERE s.person.user.id = :userId 
          AND s.company.id = :companyId
    """)
    Optional<Supplier> findByUserIdAndCompanyId(
            @Param("userId") Long userId,
            @Param("companyId") Long companyId
    );
}