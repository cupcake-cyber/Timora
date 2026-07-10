package com.timora.app.dto.booking;

import com.timora.app.model.enums.BookingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateDTO {
    private Long companyId;
    private Long serviceId;
    private Long customerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingType type;
    private String name;
    private String description;
}