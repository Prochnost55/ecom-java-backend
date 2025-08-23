package com.prochnost.ecom.backend.service.orderService;

import com.prochnost.ecom.backend.dto.orderDto.OrderListResponseDTO;
import com.prochnost.ecom.backend.dto.orderDto.OrderRequestDTO;
import com.prochnost.ecom.backend.dto.orderDto.OrderResponseDTO;
import com.prochnost.ecom.backend.exceptions.OrderNotFoundException;

import java.util.UUID;

public interface OrderService {
    OrderListResponseDTO getAllOrders();
    OrderResponseDTO getOrderById(UUID id) throws OrderNotFoundException;
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);
    boolean updateOrder(UUID id, OrderRequestDTO orderRequestDTO) throws OrderNotFoundException;
    boolean deleteOrder(UUID id);
}
