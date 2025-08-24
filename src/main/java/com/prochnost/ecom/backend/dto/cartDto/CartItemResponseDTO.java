package com.prochnost.ecom.backend.dto.cartDto;

import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CartItemResponseDTO {
    
    private UUID id;
    private ProductResponseDTO product;
    private int quantity;
    private double unitPrice;
    private double itemTotal;
}
