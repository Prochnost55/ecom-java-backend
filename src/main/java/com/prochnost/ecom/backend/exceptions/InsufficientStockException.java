package com.prochnost.ecom.backend.exceptions;

public class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }
}
