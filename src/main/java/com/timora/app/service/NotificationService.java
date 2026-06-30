package com.timora.app.service;

import com.timora.app.dto.notification.NotificationCreateDTO;
import com.timora.app.dto.notification.NotificationDTO;
import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getMyNotifications();
    NotificationDTO markAsRead(Long id);
    public NotificationDTO create(Long userId, NotificationCreateDTO dto);
    //List<NotificationDTO> findByUserAndTypeDTO(Long userId, NotificationType type);
}