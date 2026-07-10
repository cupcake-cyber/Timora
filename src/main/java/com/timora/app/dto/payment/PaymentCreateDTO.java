package com.timora.app.dto.payment;

import com.timora.app.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateDTO {
    private Long companyId;
    private Long bookingId;
    private BigDecimal amount;
    private PaymentMethod method;
}