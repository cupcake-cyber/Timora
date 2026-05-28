package com.timora.app.dto;

import com.timora.app.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookingDetailsDTO {
    public Long id;
    public String service;
    public String customer;
    public String supplier;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public BookingStatus status;
    public String description;
}