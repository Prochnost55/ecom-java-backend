package com.prochnost.ecom.backend.dto.orderDto;

import lombok.Getter;
import lombok.Setter;

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
    
    private boolean requiresPayment = true;
}
}
