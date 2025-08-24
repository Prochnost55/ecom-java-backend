package com.prochnost.ecom.backend.service.orderService;

import com.prochnost.ecom.backend.dto.orderDto.OrderListResponseDTO;
import com.prochnost.ecom.backend.dto.orderDto.OrderRequestDTO;
import com.prochnost.ecom.backend.dto.orderDto.OrderResponseDTO;
import com.prochnost.ecom.backend.exceptions.OrderNotFoundException;
import com.prochnost.ecom.backend.exceptions.ProductNotFoundException;
import com.prochnost.ecom.backend.mapper.OrderMapper;
import com.prochnost.ecom.backend.model.Order;
import com.prochnost.ecom.backend.model.OrderStatus;
import com.prochnost.ecom.backend.model.PaymentStatus;
import com.prochnost.ecom.backend.model.Product;
import com.prochnost.ecom.backend.repository.OrderRepository;
import com.prochnost.ecom.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ProductRepository productRepository;

    @Override
    public OrderListResponseDTO getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        OrderListResponseDTO orderListResponseDTO = new OrderListResponseDTO();
        
        for (Order order : orders) {
            orderListResponseDTO.getOrderList().add(OrderMapper.orderToOrderResponseDTO(order));
        }
        return orderListResponseDTO;
    }

    @Override
    public OrderResponseDTO getOrderById(UUID id) throws OrderNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        return OrderMapper.orderToOrderResponseDTO(order);
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        
        // Set user and customer information
        order.setUserId(orderRequestDTO.getUserId());
        order.setCustomerEmail(orderRequestDTO.getCustomerEmail());
        order.setCustomerName(orderRequestDTO.getCustomerName());
        order.setShippingAddress(orderRequestDTO.getShippingAddress());
        order.setOrderNotes(orderRequestDTO.getOrderNotes());
        
        // Set initial status
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        
        // Set timestamps
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        // Calculate estimated delivery (7 days from creation)
        order.setEstimatedDelivery(LocalDateTime.now().plusDays(7));
        
        // Get products by IDs
        List<Product> products = productRepository.findAllById(orderRequestDTO.getProductIds());
        order.setProducts(products);
        
        // SECURITY FIX: Calculate price from actual products, don't trust client
        double calculatedPrice = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
        order.setPrice(calculatedPrice);
        
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.orderToOrderResponseDTO(savedOrder);
    }

    @Override
    public boolean updateOrder(UUID id, OrderRequestDTO orderRequestDTO) throws OrderNotFoundException {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        
        // Update basic information
        existingOrder.setCustomerEmail(orderRequestDTO.getCustomerEmail());
        existingOrder.setCustomerName(orderRequestDTO.getCustomerName());
        existingOrder.setShippingAddress(orderRequestDTO.getShippingAddress());
        existingOrder.setOrderNotes(orderRequestDTO.getOrderNotes());
        
        // Update order status if provided
        if (orderRequestDTO.getOrderStatus() != null) {
            existingOrder.setOrderStatus(orderRequestDTO.getOrderStatus());
        }
        
        // Update payment status if provided
        if (orderRequestDTO.getPaymentStatus() != null) {
            existingOrder.setPaymentStatus(orderRequestDTO.getPaymentStatus());
        }
        
        // Update tracking number if provided
        if (orderRequestDTO.getTrackingNumber() != null) {
            existingOrder.setTrackingNumber(orderRequestDTO.getTrackingNumber());
        }
        
        // Update products if provided
        if (orderRequestDTO.getProductIds() != null && !orderRequestDTO.getProductIds().isEmpty()) {
            List<Product> products = productRepository.findAllById(orderRequestDTO.getProductIds());
            existingOrder.setProducts(products);
            
            // SECURITY FIX: Calculate price from actual products, don't trust client
            double calculatedPrice = products.stream()
                    .mapToDouble(Product::getPrice)
                    .sum();
            existingOrder.setPrice(calculatedPrice);
        }
        
        // Update timestamp
        existingOrder.setUpdatedAt(LocalDateTime.now());
        
        orderRepository.save(existingOrder);
        return true;
    }

    @Override
    public boolean deleteOrder(UUID id) {
        orderRepository.deleteById(id);
        return true;
    }
}
