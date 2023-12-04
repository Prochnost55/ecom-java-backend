package com.prochnost.ecom.backend.exceptions;

public class PriceNotFoundException extends Exception {
    public PriceNotFoundException() {
    }

    public PriceNotFoundException(String message) {
        super(message);
    }

    public PriceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
