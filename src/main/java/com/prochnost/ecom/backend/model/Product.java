package com.prochnost.ecom.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Product extends BaseModal {
    private String title;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Price price;
    
    @ManyToOne
    private Category category;
    
    @Column(length = 1000)
    private String description;
    
    private String image;
    
    // Inventory management
    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;
    
    @Column(name = "in_stock")
    private Boolean inStock = true;
    
    // Enhanced product attributes
    private String brand;
    
    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    private List<String> tags;
    
    // SEO and search optimization
    @Column(name = "search_keywords")
    private String searchKeywords;
    
    // Product status
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.ACTIVE;
    
    // Helper methods
    public boolean isAvailable() {
        return inStock && stockQuantity > 0 && status == ProductStatus.ACTIVE;
    }
    
    public void reduceStock(int quantity) {
        if (stockQuantity >= quantity) {
            stockQuantity -= quantity;
            if (stockQuantity == 0) {
                inStock = false;
            }
        }
    }
    
    public void addStock(int quantity) {
        stockQuantity += quantity;
        if (stockQuantity > 0) {
            inStock = true;
        }
    }
}
