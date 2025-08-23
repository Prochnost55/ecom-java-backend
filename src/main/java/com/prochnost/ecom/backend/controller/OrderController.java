package com.prochnost.ecom.backend.controller;

import com.prochnost.ecom.backend.dto.orderDto.OrderListResponseDTO;
import com.prochnost.ecom.backend.dto.orderDto.OrderRequestDTO;
import com.prochnost.ecom.backend.dto.orderDto.OrderResponseDTO;
import com.prochnost.ecom.backend.exceptions.OrderNotFoundException;
import com.prochnost.ecom.backend.service.orderService.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public ResponseEntity<OrderListResponseDTO> getAllOrders() {
        OrderListResponseDTO response = orderService.getAllOrders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable String id) throws OrderNotFoundException {
        OrderResponseDTO response = orderService.getOrderById(UUID.fromString(id));
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO response = orderService.createOrder(orderRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateOrder(@PathVariable String id, @Valid @RequestBody OrderRequestDTO orderRequestDTO) throws OrderNotFoundException {
        boolean response = orderService.updateOrder(UUID.fromString(id), orderRequestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable String id) {
        boolean response = orderService.deleteOrder(UUID.fromString(id));
        return ResponseEntity.ok(response);
    }
}
