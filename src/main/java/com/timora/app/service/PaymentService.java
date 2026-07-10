package com.timora.app.service;

import com.timora.app.dto.payment.PaymentCreateDTO;
import com.timora.app.dto.payment.PaymentDTO;
import com.timora.app.dto.payment.PaymentPatchDTO;
import com.timora.app.model.enums.PaymentStatus;

import java.util.List;

public interface PaymentService {
    PaymentDTO create(PaymentCreateDTO request);
    PaymentDTO patch(Long id, PaymentPatchDTO request);
    void delete(Long id);
    PaymentDTO getById(Long id);
    List<PaymentDTO> getAllByCompany();
    PaymentDTO getByBookingId(Long bookingId);
    List<PaymentDTO> getByStatus(PaymentStatus status);
}