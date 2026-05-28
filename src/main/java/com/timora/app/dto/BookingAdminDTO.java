package com.timora.app.dto;

import com.timora.app.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookingAdminDTO {
    public Long id;
    public Long companyId;
    public Long serviceId;
    public Long customerId;
    public BookingStatus status;
    public LocalDateTime createdAt;
}