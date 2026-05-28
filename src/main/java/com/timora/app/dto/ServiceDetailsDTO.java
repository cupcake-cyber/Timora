package com.timora.app.dto;

import com.timora.app.model.enums.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ServiceDetailsDTO {
    private Long id;
    private Long companyId;
    private Long supplierId;
    private String supplierName;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private ServiceStatus status;
    private LocalDateTime createdAt;
}
