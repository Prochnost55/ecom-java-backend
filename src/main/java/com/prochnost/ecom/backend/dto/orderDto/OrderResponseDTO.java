package com.prochnost.ecom.backend.dto.orderDto;

import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderResponseDTO {
    private UUID id;
    private double price;
    private List<ProductResponseDTO> products;
    private String paymentStatus = "PENDING";
    private String paymentLinkId;
    private String paymentLinkUrl;
}
}
