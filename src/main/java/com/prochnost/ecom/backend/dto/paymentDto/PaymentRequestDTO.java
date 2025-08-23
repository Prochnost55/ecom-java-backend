package com.prochnost.ecom.backend.dto.paymentDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class PaymentRequestDTO {
    @NotBlank(message = "Order ID cannot be blank")
    private String orderId;
    
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private Long amount; // Amount in smallest currency unit (e.g., paise for INR)
    
    @NotBlank(message = "Currency cannot be blank")
    private String currency = "INR";
    
    private String customerEmail;
    private String customerName;
}
