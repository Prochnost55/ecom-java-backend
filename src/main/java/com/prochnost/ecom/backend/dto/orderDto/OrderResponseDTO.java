package com.prochnost.ecom.backend.dto.orderDto;

import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.model.OrderStatus;
import com.prochnost.ecom.backend.model.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderResponseDTO {
    private UUID id;
    private double price;
    private Long userId;
    private List<ProductResponseDTO> products;
    
    // Status fields
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    
    // Payment details
    private String paymentLinkId;
    private String paymentLinkUrl;
    
    // Shipping details
    private String trackingNumber;
    private String shippingAddress;
    
    // Customer details
    private String customerEmail;
    private String customerName;
    private String orderNotes;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime estimatedDelivery;
    private LocalDateTime actualDelivery;
    
    // Computed fields
    private boolean canBeCancelled;
    private boolean isDelivered;
    private boolean isPaid;
}
