package com.timora.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateDTO {
    public Long companyId;
    public Long serviceId;
    public Long customerId;
    public Long createdByUserId;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public String name;
    public String description;
}