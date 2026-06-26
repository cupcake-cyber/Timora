package com.timora.app.repository;

import com.timora.app.model.Person;
import com.timora.app.model.enums.PersonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndCompanyId(String email, Long companyId);

    List<Person> findAllByStatus(PersonStatus status);
    List<Person> findAllByStatusAndCompanyId(PersonStatus status, Long companyId);
    Person findByIdAndStatus(Long id, PersonStatus status);
}