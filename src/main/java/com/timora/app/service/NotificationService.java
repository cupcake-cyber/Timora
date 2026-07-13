package com.timora.app.service;

import com.timora.app.dto.notification.NotificationCreateDTO;
import com.timora.app.dto.notification.NotificationDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getMyNotifications();
    NotificationDTO markAsRead(Long id);
    NotificationDTO create(Long userId, NotificationCreateDTO dto);
    NotificationDTO notifyNewBooking(Long userId, String bookingName, Long bookingId);
    NotificationDTO notifyBookingConfirmed(Long userId, String bookingName, Long bookingId);
    NotificationDTO notifyBookingCancelled(Long userId, String bookingName, Long bookingId);
    NotificationDTO notifyReminder(Long userId, String bookingName, Long bookingId, LocalDateTime dateTime);
    NotificationDTO notifyPaymentReceived(Long userId, Long bookingId, Double amount);
    NotificationDTO notifyPaymentPending(Long userId, Long bookingId, Double amount);
    NotificationDTO notifySystem(Long userId, String message, String target);

}