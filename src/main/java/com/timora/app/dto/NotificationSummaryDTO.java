package com.timora.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationSummaryDTO {

    private Long id;
    private String message;
    private String status;
    private Boolean isRead;
}