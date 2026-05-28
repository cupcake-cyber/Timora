package com.timora.app.dto;

import com.timora.app.model.enums.ServiceStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ServiceSummaryDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer duration;
    private ServiceStatus status;
    private Long supplierId;
    private String supplierName;
}
