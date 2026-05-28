package com.timora.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierAdminDTO {

    private Long supplierId;

    private String supplierName;

    private Boolean active;

    private Integer totalServices;

    private Integer totalBookings;
}