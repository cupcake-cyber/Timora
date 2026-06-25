package com.timora.app.dto.supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierCreateDTO {
    private Long companyId;
    private Long personId;
    private String specialty;
    private String notes;
}
