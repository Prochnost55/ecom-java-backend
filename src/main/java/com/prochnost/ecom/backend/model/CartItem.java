package com.prochnost.ecom.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cart_item")
public class CartItem extends BaseModal {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "quantity", nullable = false)
    private int quantity = 1;
    
    @Column(name = "unit_price", nullable = false)
    private double unitPrice; // Price at the time item was added to cart
    
    @Column(name = "item_total", nullable = false)
    private double itemTotal; // quantity * unitPrice
    
    // Helper method to calculate item total
    public void calculateItemTotal() {
        this.itemTotal = this.quantity * this.unitPrice;
    }
    
    // Helper method to update quantity
    public void updateQuantity(int newQuantity) {
        this.quantity = Math.max(1, newQuantity); // Minimum quantity is 1
        calculateItemTotal();
    }
    
    // Constructor
    public CartItem() {}
    
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice().getAmount();
        calculateItemTotal();
    }
}
