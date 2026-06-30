package com.timora.app.repository;

import com.timora.app.model.Person;
import com.timora.app.model.enums.PersonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByEmail(String email);
    List<Person> findByStatus(PersonStatus status);
    List<Person> findByCompanyIdAndStatus(Long companyId, PersonStatus status);
}