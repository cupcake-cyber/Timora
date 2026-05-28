package com.timora.app.repository;

import com.timora.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginEmail(String email);

    boolean existsByLoginEmail(String email);
}