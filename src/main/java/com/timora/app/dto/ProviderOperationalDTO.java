package com.timora.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderOperationalDTO {

    private Long supplierId;

    private Integer totalBookings;

    private Integer pendingBookings;

    private Integer completedBookings;

    private Integer cancelledBookings;
}