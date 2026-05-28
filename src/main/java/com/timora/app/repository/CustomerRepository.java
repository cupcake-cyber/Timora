package com.timora.app.repository;

import com.timora.app.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByPersonId(Long personId);

    Optional<Customer> findByPersonId(Long personId);

    void deleteByPersonId(Long personId);
}