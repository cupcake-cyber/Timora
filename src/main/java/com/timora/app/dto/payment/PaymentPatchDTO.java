package com.timora.app.dto.payment;

import com.timora.app.model.enums.PaymentMethod;
import com.timora.app.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentPatchDTO {
    private BigDecimal amount;
    private PaymentStatus status;
    private PaymentMethod method;
}