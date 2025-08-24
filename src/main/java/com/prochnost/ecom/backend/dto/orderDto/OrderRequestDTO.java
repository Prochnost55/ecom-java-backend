package com.prochnost.ecom.backend.dto.orderDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderRequestDTO {
    @NotNull(message = "Product IDs cannot be null")
    @NotEmpty(message = "Order must contain at least one product")
    private List<UUID> productIds;
    
    @Positive(message = "Total price must be positive")
    private double totalPrice;
    
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    
    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;
    
    @Email(message = "Valid customer email is required")
    @NotBlank(message = "Customer email is required")
    private String customerEmail;
    
    @NotBlank(message = "Customer name is required")
    private String customerName;
    
    private String orderNotes;
    
    private boolean requiresPayment = true;
}
