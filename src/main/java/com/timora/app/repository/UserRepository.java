package com.timora.app.repository;

import com.timora.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginEmail(String loginEmail);
    List<User> findByCompanyId(Long companyId);
    boolean existsByLoginEmail(String loginEmail);
}
