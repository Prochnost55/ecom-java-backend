package com.prochnost.ecom.backend.dto.cartDto;

import com.prochnost.ecom.backend.model.CartStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CartResponseDTO {
    
    private UUID id;
    private Long userId;
    private List<CartItemResponseDTO> cartItems;
    private double totalAmount;
    private int totalItems;
    private CartStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
