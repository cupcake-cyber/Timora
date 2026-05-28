package com.timora.app.dto;

import com.timora.app.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookingStatusDTO {
    public Long id;
    public BookingStatus status;
}