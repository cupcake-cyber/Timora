package com.timora.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationDTO {

    private Long id;

    private String type;
    private String message;
    private String status;

    private Boolean isRead;

    private LocalDateTime createdAt;
    private LocalDateTime sentAt;

    private String target;
}