//package com.timora.app.service.impl;
//
//import com.timora.app.dto.NotificationDTO;
//import com.timora.app.model.Notification;
//import com.timora.app.model.User;
//import com.timora.app.model.enums.NotificationStatus;
//import com.timora.app.model.enums.NotificationType;
//import com.timora.app.repository.NotificationRepository;
//import com.timora.app.repository.UserRepository;
//import com.timora.app.service.NotificationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class NotificationServiceImpl implements NotificationService {
//
//    private final NotificationRepository notificationRepository;
//    private final UserRepository userRepository;
//
//    // =========================
//    // READ ALL
//    // =========================
//
//    @Override
//    public List<NotificationDTO> findAllDTO() {
//        return notificationRepository.findAll()
//                .stream()
//                .map(this::mapToDTO)
//                .toList();
//    }
//
//    // =========================
//    // BY ID
//    // =========================
//
//    @Override
//    public NotificationDTO findByIdDTO(Long id) {
//        Notification n = notificationRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Notification not found"));
//
//        return mapToDTO(n);
//    }
//
//    // =========================
//    // BY USER
//    // =========================
//
//    @Override
//    public List<NotificationDTO> findByUserDTO(Long userId) {
//        return notificationRepository.findByUser(userId)
//                .stream()
//                .map(this::mapToDTO)
//                .toList();
//    }
//
//    // =========================
//    // UNREAD
//    // =========================
//
//    @Override
//    public List<NotificationDTO> findUnreadByUserDTO(Long userId) {
//        return notificationRepository.findUnreadByUser(userId)
//                .stream()
//                .map(this::mapToDTO)
//                .toList();
//    }
//
//    // =========================
//    // BY TYPE
//    // =========================
//
//    @Override
//    public List<NotificationDTO> findByUserAndTypeDTO(Long userId, NotificationType type) {
//        return notificationRepository.findByUserAndType(userId, type)
//                .stream()
//                .map(this::mapToDTO)
//                .toList();
//    }
//
//    // =========================
//    // CREATE NOTIFICATION
//    // =========================
//
//    @Override
//    public NotificationDTO sendNotification(Long userId, String message, NotificationType type) {
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Notification notification = new Notification();
//
//        notification.setUser(user);
//        notification.setMessage(message);
//        notification.setType(type);
//        notification.setStatus(NotificationStatus.PENDING);
//        notification.setIsRead(false);
//        notification.setCreatedAt(LocalDateTime.now());
//        notification.setSentAt(null);
//
//        return mapToDTO(notificationRepository.save(notification));
//    }
//
//    // =========================
//    // UPDATE
//    // =========================
//
//    @Override
//    public NotificationDTO update(Long id, NotificationDTO dto) {
//
//        Notification existing = notificationRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Notification not found"));
//
//        existing.setMessage(dto.getMessage());
//        existing.setTarget(dto.getTarget());
//
//        if (dto.getType() != null) {
//            existing.setType(NotificationType.valueOf(dto.getType()));
//        }
//
//        if (dto.getStatus() != null) {
//            existing.setStatus(NotificationStatus.valueOf(dto.getStatus()));
//        }
//
//        return mapToDTO(notificationRepository.save(existing));
//    }
//
//    // =========================
//    // MARK AS READ
//    // =========================
//
//    @Override
//    public NotificationDTO markAsRead(Long id) {
//
//        Notification notification = notificationRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Notification not found"));
//
//        notification.setIsRead(true);
//
//        return mapToDTO(notificationRepository.save(notification));
//    }
//
//    // =========================
//    // DELETE
//    // =========================
//
//    @Override
//    public void delete(Long id) {
//
//        Notification existing = notificationRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Notification not found"));
//
//        notificationRepository.delete(existing);
//    }
//
//    // =========================
//    // MAPPER
//    // =========================
//
//    private NotificationDTO mapToDTO(Notification n) {
//
//        NotificationDTO dto = new NotificationDTO();
//
//        dto.setId(n.getId());
//        dto.setType(n.getType().name());
//        dto.setMessage(n.getMessage());
//        dto.setStatus(n.getStatus().name());
//
//        dto.setIsRead(n.getIsRead());
//        dto.setCreatedAt(n.getCreatedAt());
//        dto.setSentAt(n.getSentAt());
//        dto.setTarget(n.getTarget());
//
//        return dto;
//    }
//}