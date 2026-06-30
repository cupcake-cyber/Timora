package com.timora.app.service.impl;

import com.timora.app.dto.notification.NotificationCreateDTO;
import com.timora.app.dto.notification.NotificationDTO;
import com.timora.app.dto.security.CurrentUser;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.model.Notification;
import com.timora.app.model.User;
import com.timora.app.model.enums.NotificationStatus;
import com.timora.app.model.enums.NotificationType;
import com.timora.app.repository.NotificationRepository;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.NotificationService;
import com.timora.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SecurityHelper securityHelper;
    private final UserService userService;

    @Override
    public NotificationDTO create(Long userId, NotificationCreateDTO dto) {
        User user = userService.findById(userId);

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(dto.getMessage());
        notification.setType(dto.getType());
        notification.setStatus(NotificationStatus.PENDING);
        notification.setType(dto.getType());
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setTarget(dto.getTarget());

        Notification saved = notificationRepository.save(notification);

        return toDTO(saved);
    }

    @Override
    public List<NotificationDTO> getMyNotifications() {
        CurrentUser currentUser = securityHelper.getCurrentUser();

        List<Notification> notifications =
                notificationRepository.findByUserId(currentUser.getUserId());

        return notifications.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public NotificationDTO markAsRead(Long id) {
        CurrentUser currentUser = securityHelper.getCurrentUser();
        if(!notificationRepository.existsByIdAndUserId(id, currentUser.getUserId())) {
            throw new ForbiddenException("You are not allowed to mark this notification as read");
        }
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setIsRead(true);

        return toDTO(notification);
    }


    private NotificationDTO toDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setType(notification.getType());
        dto.setMessage(notification.getMessage());
        dto.setStatus(notification.getStatus());
        dto.setIsRead(notification.getIsRead());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setTarget(notification.getTarget());
        return dto;
    }
}