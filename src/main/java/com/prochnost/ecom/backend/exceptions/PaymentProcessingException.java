package com.prochnost.ecom.backend.exceptions;

public class PaymentProcessingException extends Exception {
    public PaymentProcessingException() {
    }

    public PaymentProcessingException(String message) {
        super(message);
    }

    public PaymentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
