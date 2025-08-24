package com.prochnost.ecom.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cart")
public class Cart extends BaseModal {
    
    @Column(name = "user_id", nullable = false)
    private Long userId; // Reference to user from auth service
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();
    
    @Column(name = "total_amount")
    private double totalAmount = 0.0;
    
    @Column(name = "total_items")
    private int totalItems = 0;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CartStatus status = CartStatus.ACTIVE;
    
    // Helper method to calculate totals
    public void calculateTotals() {
        this.totalAmount = cartItems.stream()
                .mapToDouble(CartItem::getItemTotal)
                .sum();
        this.totalItems = cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Helper method to add item
    public void addItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this);
        calculateTotals();
    }
    
    // Helper method to remove item
    public void removeItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setCart(null);
        calculateTotals();
    }
}
