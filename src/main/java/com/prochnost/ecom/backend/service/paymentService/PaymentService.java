package com.prochnost.ecom.backend.service.paymentService;

import com.prochnost.ecom.backend.dto.paymentDto.PaymentRequestDTO;
import com.prochnost.ecom.backend.dto.paymentDto.PaymentResponseDTO;
import com.stripe.exception.StripeException;

public interface PaymentService {
    PaymentResponseDTO makePayment(PaymentRequestDTO paymentRequestDTO) throws StripeException;
}