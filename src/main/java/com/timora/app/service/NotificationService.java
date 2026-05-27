package com.timora.app.service;

import com.timora.app.dto.NotificationDTO;
import com.timora.app.model.Notification;
import com.timora.app.model.enums.NotificationType;

import java.util.List;
import java.util.Optional;

public interface NotificationService {

    List<Notification> findAll();

    Optional<Notification> findById(Long id);

    List<Notification> findByUser(Long userId);

    List<Notification> findUnreadByUser(Long userId);

    Notification save(Notification notification);

    Notification update(Long id, Notification notification);

    Notification markAsRead(Long id);

    void delete(Long id);

    NotificationDTO sendNotification(
            Long userId,
            String message,
            NotificationType type
    );

    List<NotificationDTO> getUserNotifications(Long userId);

    List<Notification> findByUserAndType(
            Long userId,
            NotificationType type
    );
}