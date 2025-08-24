package com.prochnost.ecom.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "ECOM_ORDER")
@Getter
@Setter
public class Order extends BaseModal{
    
    @Column(name = "total_price", nullable = false)
    private double price;
    
    @Column(name = "user_id", nullable = false)
    private Long userId; // Reference to user from auth service
    
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus = OrderStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    @Column(name = "payment_link_id")
    private String paymentLinkId; // Stripe payment link ID
    
    @Column(name = "payment_link_url")
    private String paymentLinkUrl; // Stripe payment link URL
    
    @Column(name = "tracking_number")
    private String trackingNumber; // Shipping tracking number
    
    @Column(name = "shipping_address", length = 500)
    private String shippingAddress;
    
    @Column(name = "customer_email")
    private String customerEmail;
    
    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "order_notes", length = 1000)
    private String orderNotes;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "estimated_delivery")
    private LocalDateTime estimatedDelivery;
    
    @Column(name = "actual_delivery")
    private LocalDateTime actualDelivery;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "order_products",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;
    
    // Helper methods
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markAsConfirmed() {
        this.orderStatus = OrderStatus.CONFIRMED;
        this.paymentStatus = PaymentStatus.COMPLETED;
        updateTimestamp();
    }
    
    public void markAsShipped(String trackingNumber) {
        this.orderStatus = OrderStatus.SHIPPED;
        this.trackingNumber = trackingNumber;
        updateTimestamp();
    }
    
    public void markAsDelivered() {
        this.orderStatus = OrderStatus.DELIVERED;
        this.actualDelivery = LocalDateTime.now();
        updateTimestamp();
    }
    
    public void cancel() {
        this.orderStatus = OrderStatus.CANCELLED;
        updateTimestamp();
    }
    
    public boolean canBeCancelled() {
        return orderStatus == OrderStatus.PENDING || orderStatus == OrderStatus.CONFIRMED;
    }
    
    public boolean isDelivered() {
        return orderStatus == OrderStatus.DELIVERED;
    }
    
    public boolean isPaid() {
        return paymentStatus == PaymentStatus.COMPLETED;
    }
}