package com.prochnost.ecom.backend.service.orderService;

import com.prochnost.ecom.backend.dto.orderDto.OrderListResponseDTO;
import com.prochnost.ecom.backend.dto.orderDto.OrderRequestDTO;
import com.prochnost.ecom.backend.dto.orderDto.OrderResponseDTO;
import com.prochnost.ecom.backend.exceptions.OrderNotFoundException;
import com.prochnost.ecom.backend.exceptions.ProductNotFoundException;
import com.prochnost.ecom.backend.mapper.OrderMapper;
import com.prochnost.ecom.backend.model.Order;
import com.prochnost.ecom.backend.model.Product;
import com.prochnost.ecom.backend.repository.OrderRepository;
import com.prochnost.ecom.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                
        List<Product> products = productRepository.findAllById(orderRequestDTO.getProductIds());
        existingOrder.setProducts(products);
        
        // SECURITY FIX: Calculate price from actual products, don't trust client
        double calculatedPrice = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
        existingOrder.setPrice(calculatedPrice);
        
        orderRepository.save(existingOrder);
        return true;
    }

    @Override
    public boolean deleteOrder(UUID id) {
        orderRepository.deleteById(id);
        return true;
    }
}
