package com.timora.app.dto.notifications;

import com.timora.app.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    private Long id;

    private NotificationType type;

    private String message;

    private Boolean isRead;

    private String createdAt;

}