package com.prochnost.ecom.backend.dto.cartDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class CheckoutRequestDTO {
    
    @NotNull(message = "Cart ID cannot be null")
    private UUID cartId;
    
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    
    // Optional fields for checkout
    private String customerEmail;
    private String customerName;
    private String shippingAddress;
    private String paymentMethod = "STRIPE";
}
