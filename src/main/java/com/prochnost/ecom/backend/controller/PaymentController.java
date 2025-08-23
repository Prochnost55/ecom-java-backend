package com.prochnost.ecom.backend.controller;

import com.prochnost.ecom.backend.dto.paymentDto.PaymentRequestDTO;
import com.prochnost.ecom.backend.dto.paymentDto.PaymentResponseDTO;
import com.prochnost.ecom.backend.service.paymentService.PaymentService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/payments")
@Tag(name = "Payment Management", description = "Payment processing endpoints")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Create payment link", description = "Create a Stripe payment link for order payment")
    @PostMapping("/create-link")
    public ResponseEntity<PaymentResponseDTO> createPaymentLink(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            PaymentResponseDTO response = paymentService.makePayment(paymentRequestDTO);
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            // In a real application, you'd have proper error handling
            throw new RuntimeException("Payment processing failed: " + e.getMessage(), e);
        }
    }
    
    @Operation(summary = "Payment success callback", description = "Handle successful payment callback")
    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess(@RequestParam String orderId) {
        // Handle successful payment - update order status, send confirmation email, etc.
        return ResponseEntity.ok("Payment successful for order: " + orderId);
    }
    
    @Operation(summary = "Payment cancel callback", description = "Handle cancelled payment callback")
    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancel(@RequestParam String orderId) {
        // Handle cancelled payment - log cancellation, notify user, etc.
        return ResponseEntity.ok("Payment cancelled for order: " + orderId);
    }
}
