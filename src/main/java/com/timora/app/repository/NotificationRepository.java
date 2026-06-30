package com.timora.app.repository;

import com.timora.app.model.Notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
}