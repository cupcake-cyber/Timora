package com.timora.app.dto.notification;

import com.timora.app.model.enums.NotificationStatus;
import com.timora.app.model.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationDTO {
    private Long id;
    private NotificationType type;
    private String message;
    private NotificationStatus status;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private String target;
}