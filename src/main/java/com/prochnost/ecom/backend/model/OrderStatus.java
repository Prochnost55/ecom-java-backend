package com.prochnost.ecom.backend.model;

public enum OrderStatus {
    PENDING,        // Order created, awaiting payment
    CONFIRMED,      // Payment confirmed, order processing
    PROCESSING,     // Order being prepared
    SHIPPED,        // Order shipped/dispatched
    OUT_FOR_DELIVERY, // Order out for delivery
    DELIVERED,      // Order successfully delivered
    CANCELLED,      // Order cancelled by user/admin
    REFUNDED        // Order refunded
}
