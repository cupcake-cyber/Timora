package com.timora.app.controller;

import com.timora.app.dto.NotificationDTO;
import com.timora.app.model.Notification;
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

    @GetMapping
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(notificationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getById(@PathVariable Long id) {

        return notificationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                notificationService.getUserNotifications(userId)
        );
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                notificationService.findUnreadByUser(userId)
        );
    }

    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<List<Notification>> getByType(
            @PathVariable Long userId,
            @PathVariable NotificationType type
    ) {

        return ResponseEntity.ok(
                notificationService.findByUserAndType(userId, type)
        );
    }

    @PostMapping
    public ResponseEntity<Notification> create(
            @RequestBody Notification notification
    ) {

        Notification saved = notificationService.save(notification);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/send")
    public ResponseEntity<NotificationDTO> sendNotification(
            @RequestParam Long userId,
            @RequestParam String message,
            @RequestParam NotificationType type
    ) {

        NotificationDTO notification = notificationService.sendNotification(
                userId,
                message,
                type
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> update(
            @PathVariable Long id,
            @RequestBody Notification notification
    ) {

        return ResponseEntity.ok(
                notificationService.update(id, notification)
        );
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                notificationService.markAsRead(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        notificationService.delete(id);

        return ResponseEntity.noContent().build();
    }
}