package com.timora.app.dto.service;

import com.timora.app.model.enums.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicePatchDTO {
    private Long supplierId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private ServiceStatus status;
}