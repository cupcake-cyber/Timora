package com.timora.app.dto.supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    private Long id;
    private Long companyId;
    private Long personId;
    private String specialty;
    private String notes;
    private LocalDateTime createdAt;
}
