package com.timora.app.repository;

import com.timora.app.model.User;
import com.timora.app.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
    SELECT u FROM User u
    JOIN FETCH u.company
    JOIN FETCH u.person
    WHERE u.email = :email
    """)
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    Optional<User> findByPersonId(Long personId);
    User findByIdAndStatus(Long id, UserStatus status);
}