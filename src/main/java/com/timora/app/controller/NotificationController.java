package com.timora.app.controller;

import com.timora.app.dto.NotificationDTO;
import com.timora.app.model.enums.NotificationType;
import com.timora.app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // =========================
    // ALL NOTIFICATIONS
    // =========================
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAll() {
        return ResponseEntity.ok(notificationService.findAllDTO());
    }

    // =========================
    // BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.findByIdDTO(id));
    }

    // =========================
    // BY USER
    // =========================
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.findByUserDTO(userId));
    }

    // =========================
    // UNREAD
    // =========================
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationDTO>> getUnread(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.findUnreadByUserDTO(userId));
    }

    // =========================
    // BY TYPE
    // =========================
    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<List<NotificationDTO>> getByType(
            @PathVariable Long userId,
            @PathVariable NotificationType type
    ) {
        return ResponseEntity.ok(notificationService.findByUserAndTypeDTO(userId, type));
    }

    // =========================
    // SEND NOTIFICATION
    // =========================
    @PostMapping
    public ResponseEntity<NotificationDTO> send(
            @RequestParam Long userId,
            @RequestParam String message,
            @RequestParam NotificationType type
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.sendNotification(userId, message, type));
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<NotificationDTO> update(
            @PathVariable Long id,
            @RequestBody NotificationDTO dto
    ) {
        return ResponseEntity.ok(notificationService.update(id, dto));
    }

    // =========================
    // MARK AS READ
    // =========================
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}