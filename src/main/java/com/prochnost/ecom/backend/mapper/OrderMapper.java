package com.prochnost.ecom.backend.mapper;

import com.prochnost.ecom.backend.dto.orderDto.OrderResponseDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.model.Order;
import com.prochnost.ecom.backend.model.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    
    public static OrderResponseDTO orderToOrderResponseDTO(Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setPrice(order.getPrice());
        orderResponseDTO.setUserId(order.getUserId());
        
        // Status fields
        orderResponseDTO.setOrderStatus(order.getOrderStatus());
        orderResponseDTO.setPaymentStatus(order.getPaymentStatus());
        
        // Payment details
        orderResponseDTO.setPaymentLinkId(order.getPaymentLinkId());
        orderResponseDTO.setPaymentLinkUrl(order.getPaymentLinkUrl());
        
        // Shipping details
        orderResponseDTO.setTrackingNumber(order.getTrackingNumber());
        orderResponseDTO.setShippingAddress(order.getShippingAddress());
        
        // Customer details
        orderResponseDTO.setCustomerEmail(order.getCustomerEmail());
        orderResponseDTO.setCustomerName(order.getCustomerName());
        orderResponseDTO.setOrderNotes(order.getOrderNotes());
        
        // Timestamps
        orderResponseDTO.setCreatedAt(order.getCreatedAt());
        orderResponseDTO.setUpdatedAt(order.getUpdatedAt());
        orderResponseDTO.setEstimatedDelivery(order.getEstimatedDelivery());
        orderResponseDTO.setActualDelivery(order.getActualDelivery());
        
        // Computed fields
        orderResponseDTO.setCanBeCancelled(order.canBeCancelled());
        orderResponseDTO.setDelivered(order.isDelivered());
        orderResponseDTO.setPaid(order.isPaid());
        
        // Products mapping
        List<ProductResponseDTO> productResponseDTOs = new ArrayList<>();
        if (order.getProducts() != null) {
            for (Product product : order.getProducts()) {
                productResponseDTOs.add(ProductMapper.productToProductResponseDTO(product));
            }
        }
        orderResponseDTO.setProducts(productResponseDTOs);
        
        return orderResponseDTO;
    }
}
