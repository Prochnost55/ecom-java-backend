package com.prochnost.ecom.backend.exceptions;

public class CartItemNotFoundException extends Exception {
    public CartItemNotFoundException(String message) {
        super(message);
    }
}
