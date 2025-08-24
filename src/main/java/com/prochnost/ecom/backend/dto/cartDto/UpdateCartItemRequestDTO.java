package com.prochnost.ecom.backend.dto.cartDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class UpdateCartItemRequestDTO {
    
    @NotNull(message = "Cart item ID cannot be null")
    private UUID cartItemId;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
