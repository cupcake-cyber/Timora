package com.timora.app.repository;

import com.timora.app.model.User;
import com.timora.app.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByLoginEmail(String loginEmail);
    List<User> findByCompanyId(Long companyId);
    boolean existsByLoginEmail(String email);
    @Query("""
        SELECT u
        FROM User u
        LEFT JOIN FETCH u.person p
        WHERE u.loginEmail = :email
        AND u.status = :status
    """)
    Optional<User> findByEmailAndStatus(
            @Param("email") String email,
            @Param("status") UserStatus status
    );
}
