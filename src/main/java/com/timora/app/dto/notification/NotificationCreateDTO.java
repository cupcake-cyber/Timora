package com.timora.app.dto.notification;

import com.timora.app.model.enums.NotificationStatus;
import com.timora.app.model.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationCreateDTO {
    private NotificationType type;
    private String message;
    private NotificationStatus status;
    private String target;
}