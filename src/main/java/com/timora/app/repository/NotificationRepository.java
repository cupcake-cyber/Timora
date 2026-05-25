package com.timora.app.repository;

import com.timora.app.model.Notification;
import com.timora.app.model.enums.NotificationStatus;
import com.timora.app.model.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);

    List<Notification> findByStatus(NotificationStatus status);

    List<Notification> findByType(NotificationType type);
}