package com.prochnost.ecom.backend.model;

public enum CartStatus {
    ACTIVE,     // Cart is active and can be modified
    CHECKOUT,   // Cart is in checkout process
    CONVERTED,  // Cart has been converted to order
    ABANDONED   // Cart was abandoned by user
}
