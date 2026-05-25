package com.timora.app.repository;

import com.timora.app.model.Configuration;
import com.timora.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigurationRepository
        extends JpaRepository<Configuration, Long> {

    Optional<Configuration> findByUser(User user);

    Optional<Configuration> findByUserId(Long userId);
}