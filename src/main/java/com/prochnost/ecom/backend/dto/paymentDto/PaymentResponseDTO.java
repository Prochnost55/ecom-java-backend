package com.prochnost.ecom.backend.dto.paymentDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResponseDTO {
    private String paymentLinkId;
    private String paymentLinkUrl;
    private String status;
    private String orderId;
    private Long amount;
    private String currency;
}
