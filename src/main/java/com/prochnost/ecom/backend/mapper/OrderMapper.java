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
