package com.timora.app.repository;

import com.timora.app.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndCompanyId(String email, Long companyId);

    @Query("""
        SELECT p FROM Person p
        LEFT JOIN FETCH p.user
        WHERE p.status = 'ACTIVE'
        AND (:isOwner = true OR p.company.id = :companyId)
    """)
    List<Person> findAll(Boolean isOwner, Long companyId);

    @Query("""
        SELECT p FROM Person p
        LEFT JOIN FETCH p.user
        WHERE p.status = 'ACTIVE'
        AND (:companyId IS NULL OR p.company.id = :companyId)
    """)
    List<Person> findAllActiveByCompany(Long companyId);

    @Query("""
        SELECT p FROM Person p
        LEFT JOIN FETCH p.user
        WHERE p.id = :id
        AND p.status = 'ACTIVE'
    """)
    Optional<Person> findActiveById(Long id);
}