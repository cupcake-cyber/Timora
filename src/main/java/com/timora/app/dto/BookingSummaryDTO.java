package com.timora.app.dto;

import com.timora.app.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookingSummaryDTO {
    public Long id;
    public String serviceName;
    public String customerName;
    public LocalDateTime startTime;
    public BookingStatus status;
}