package com.prochnost.ecom.backend.exceptions;

public class CartNotFoundException extends Exception {
    public CartNotFoundException(String message) {
        super(message);
    }
}
