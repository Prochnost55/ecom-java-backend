package com.prochnost.ecom.backend.dto.cartDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class AddToCartRequestDTO {
    
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    
    @NotNull(message = "Product ID cannot be null")
    private UUID productId;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity = 1;
}
