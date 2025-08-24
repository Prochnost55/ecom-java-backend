package com.prochnost.ecom.backend.model;

public enum PaymentStatus {
    PENDING,        // Payment not yet processed
    PROCESSING,     // Payment being processed
    COMPLETED,      // Payment successfully completed
    FAILED,         // Payment failed
    CANCELLED,      // Payment cancelled
    REFUNDED,       // Payment refunded
    PARTIAL_REFUND  // Partial refund processed
}
