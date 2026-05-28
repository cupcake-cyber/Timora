package com.timora.app.repository;

import com.timora.app.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    boolean existsByPersonId(Long personId);

    boolean existsByIdAndCompanyId(Long id, Long companyId);

    boolean existsByPersonIdAndCompanyId(Long personId, Long companyId);

    // =========================
    // FINDERS (SAFE / SCOPED)
    // =========================

    Optional<Supplier> findByIdAndCompanyId(Long id, Long companyId);

    Optional<Supplier> findByPersonId(Long personId);

    Optional<Supplier> findByPersonIdAndCompanyId(Long personId, Long companyId);

    List<Supplier> findByCompanyId(Long companyId);

    // =========================
    // DELETE (SOFT OR HARD USAGE SUPPORT)
    // =========================

    void deleteByPersonId(Long personId);

    void deleteByIdAndCompanyId(Long id, Long companyId);

    @Query("""
        SELECT s
        FROM Supplier s
        JOIN Person p ON p.id = s.person.id
        WHERE p.user.id = :userId
          AND s.company.id = :companyId
    """)
    Optional<Supplier> findByUserIdAndCompanyId(
            @Param("userId") Long userId,
            @Param("companyId") Long companyId
    );
}