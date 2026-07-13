package com.timora.app.service.impl;

import com.timora.app.dto.notification.NotificationCreateDTO;
import com.timora.app.dto.notification.NotificationDTO;
import com.timora.app.dto.security.CurrentUser;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.model.Configuration;
import com.timora.app.model.Notification;
import com.timora.app.model.User;
import com.timora.app.model.enums.NotificationStatus;
import com.timora.app.model.enums.NotificationType;
import com.timora.app.repository.ConfigurationRepository;
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
    private final ConfigurationRepository configurationRepository;
    private final SecurityHelper securityHelper;
    private final UserService userService;

    // =========================
    // MÉTODOS BASE
    // =========================

    @Override
    public NotificationDTO create(Long userId, NotificationCreateDTO dto) {
        User user = userService.findById(userId);

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(dto.getMessage());
        notification.setType(dto.getType());
        notification.setStatus(NotificationStatus.PENDING);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setTarget(dto.getTarget());

        Notification saved = notificationRepository.save(notification);
        return toDTO(saved);
    }

    private NotificationDTO createNotification(Long userId, NotificationType type, String message, String target) {
        // Verificar si el usuario tiene habilitadas las notificaciones para este tipo
        if (!shouldSendNotification(userId, type)) {
            return null;
        }

        User user = userService.findById(userId);

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setTarget(target);

        Notification saved = notificationRepository.save(notification);
        return toDTO(saved);
    }

    private boolean shouldSendNotification(Long userId, NotificationType type) {
        Configuration config = configurationRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Configuration not found"));
        if (config == null) {
            return true; // Si no hay configuración, enviar por defecto
        }

        switch (type) {
            case BOOKING:
                return config.getNotifyAppointments() != null && config.getNotifyAppointments();
            case CANCELLATION:
                return config.getNotifyCancellations() != null && config.getNotifyCancellations();
            case REMINDER:
                return config.getNotifyReminders() != null && config.getNotifyReminders();

            case SYSTEM:
                return true; // Las notificaciones del sistema siempre se envían
            default:
                return true;
        }
    }

    // =========================
    // MÉTODOS PREDETERMINADOS
    // =========================

    /**
     * Notificación de nueva reserva (booking)
     */
    @Override
    public NotificationDTO notifyNewBooking(Long userId, String bookingName, Long bookingId) {
        String message = String.format("Nueva reserva: %s", bookingName);
        String target = String.format("booking:%d", bookingId);
        return createNotification(userId, NotificationType.BOOKING, message, target);
    }

    /**
     * Notificación de confirmación de reserva
     */
    @Override
    public NotificationDTO notifyBookingConfirmed(Long userId, String bookingName, Long bookingId) {
        String message = String.format("Reserva confirmada: %s", bookingName);
        String target = String.format("booking:%d", bookingId);
        return createNotification(userId, NotificationType.BOOKING, message, target);
    }

    /**
     * Notificación de cancelación de reserva
     */
    @Override
    public NotificationDTO notifyBookingCancelled(Long userId, String bookingName, Long bookingId) {
        String message = String.format("Reserva cancelada: %s", bookingName);
        String target = String.format("booking:%d", bookingId);
        return createNotification(userId, NotificationType.CANCELLATION, message, target);
    }

    /**
     * Notificación de recordatorio (próxima cita)
     */
    @Override
    public NotificationDTO notifyReminder(Long userId, String bookingName, Long bookingId, LocalDateTime dateTime) {
        String message = String.format("Recordatorio: %s - %s", bookingName, formatDateTime(dateTime));
        String target = String.format("booking:%d", bookingId);
        return createNotification(userId, NotificationType.REMINDER, message, target);
    }

    /**
     * Notificación de pago realizado
     */
    @Override
    public NotificationDTO notifyPaymentReceived(Long userId, Long bookingId, Double amount) {
        String message = String.format("Pago recibido: S/. %.2f para reserva #%d", amount, bookingId);
        String target = String.format("payment:booking:%d", bookingId);
        return createNotification(userId, NotificationType.PAYMENT, message, target);
    }

    /**
     * Notificación de pago pendiente
     */
    @Override
    public NotificationDTO notifyPaymentPending(Long userId, Long bookingId, Double amount) {
        String message = String.format("Pago pendiente: S/. %.2f para reserva #%d", amount, bookingId);
        String target = String.format("payment:booking:%d", bookingId);
        return createNotification(userId, NotificationType.PAYMENT, message, target);
    }

    /**
     * Notificación del sistema
     */
    @Override
    public NotificationDTO notifySystem(Long userId, String message, String target) {
        return createNotification(userId, NotificationType.SYSTEM, message, target);
    }

    // =========================
    // MÉTODOS EXISTENTES
    // =========================

    @Override
    public List<NotificationDTO> getMyNotifications() {
        CurrentUser currentUser = securityHelper.getCurrentUser();
        List<Notification> notifications = notificationRepository.findByUserId(currentUser.getUserId());
        return notifications.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public NotificationDTO markAsRead(Long id) {
        CurrentUser currentUser = securityHelper.getCurrentUser();
        if (!notificationRepository.existsByIdAndUserId(id, currentUser.getUserId())) {
            throw new ForbiddenException("You are not allowed to mark this notification as read");
        }
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setIsRead(true);
        return toDTO(notification);
    }

    // =========================
    // HELPERS
    // =========================

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
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