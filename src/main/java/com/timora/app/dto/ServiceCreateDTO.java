package com.timora.app.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ServiceCreateDTO {
    @NotNull
    private Long supplierId;
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;
    @NotNull
    @Min(1)
    private Integer duration;
}
