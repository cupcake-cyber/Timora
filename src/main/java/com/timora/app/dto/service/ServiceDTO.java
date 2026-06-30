package com.timora.app.dto.service;

import com.timora.app.model.enums.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    private Long id;
    private Long companyId;
    private String companyName;
    private Long supplierId;
    private String supplierName;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private ServiceStatus status;
    private LocalDateTime createdAt;
}