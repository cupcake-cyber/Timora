package com.timora.app.service.impl;

import com.timora.app.dto.NotificationDTO;
import com.timora.app.model.Notification;
import com.timora.app.model.enums.NotificationStatus;
import com.timora.app.model.enums.NotificationType;
import com.timora.app.repository.NotificationRepository;
import com.timora.app.repository.UserRepository;
import com.timora.app.model.User;
import com.timora.app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id);
    }


    @Override
    public List<Notification> findByUser(Long userId) { return notificationRepository.findByUserIdOrderBySentAtDesc(userId); }

    @Override
    public List<Notification> findUnreadByUser(Long userId) { return notificationRepository.findByUserIdAndStatusNot(userId, NotificationStatus.READ); }

    @Override
    public Notification save(Notification notification) {
        if (notification.getSentAt() == null) {
            notification.setSentAt(LocalDateTime.now());
        }

        if (notification.getStatus() == null) {
            notification.setStatus(NotificationStatus.PENDING);
        }

        return notificationRepository.save(notification);
    }

    @Override
    public Notification update(Long id, Notification notification) {

        Notification existing = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        existing.setType(notification.getType());
        existing.setMessage(notification.getMessage());
        existing.setStatus(notification.getStatus());
        existing.setTarget(notification.getTarget());

        return notificationRepository.save(existing);
    }

    @Override
    public Notification markAsRead(Long id) {

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setStatus(NotificationStatus.READ);

        return notificationRepository.save(notification);
    }

    @Override
    public void delete(Long id) {

        Notification existing = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notificationRepository.delete(existing);
    }
    @Override
    public List<Notification> findByUserAndType(
            Long userId,
            NotificationType type
    ) {

        return notificationRepository
                .findByUserIdAndType(userId, type);
    }

    @Override
    public NotificationDTO sendNotification(
            Long userId,
            String message,
            NotificationType type
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setSentAt(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);

        return mapToDTO(saved);
    }

    @Override
    public List<NotificationDTO> getUserNotifications(Long userId) {

        return notificationRepository
                .findByUserIdOrderBySentAtDesc(userId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private NotificationDTO mapToDTO(Notification notification) {

        NotificationDTO dto = new NotificationDTO();

        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());

        dto.setType(notification.getType().name());
        dto.setStatus(notification.getStatus().name());

        dto.setSentAt(notification.getSentAt());
        dto.setTarget(notification.getTarget());

        return dto;
    }

}