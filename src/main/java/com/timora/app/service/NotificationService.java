//package com.timora.app.service;
//
//import com.timora.app.dto.NotificationDTO;
//import com.timora.app.model.enums.NotificationType;
//
//import java.util.List;
//
//public interface NotificationService {
//
//    // =========================
//    // READ DTO
//    // =========================
//    List<NotificationDTO> findAllDTO();
//
//    NotificationDTO findByIdDTO(Long id);
//
//    List<NotificationDTO> findByUserDTO(Long userId);
//
//    List<NotificationDTO> findUnreadByUserDTO(Long userId);
//
//    List<NotificationDTO> findByUserAndTypeDTO(Long userId, NotificationType type);
//
//    // =========================
//    // ACTIONS
//    // =========================
//    NotificationDTO sendNotification(Long userId, String message, NotificationType type);
//
//    NotificationDTO update(Long id, NotificationDTO dto);
//
//    NotificationDTO markAsRead(Long id);
//
//    void delete(Long id);
//}