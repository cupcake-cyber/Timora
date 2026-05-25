package com.timora.app.repository;

import com.timora.app.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String email);
    List<Person> findByCompanyId(Long companyId);
    boolean existsByEmail(String email);
}
