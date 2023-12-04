package com.prochnost.ecom.backend.repository;

import com.prochnost.ecom.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}