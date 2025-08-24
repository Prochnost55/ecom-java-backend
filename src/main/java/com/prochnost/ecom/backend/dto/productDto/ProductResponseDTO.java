package com.prochnost.ecom.backend.dto.productDto;

import com.prochnost.ecom.backend.model.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductResponseDTO {
    private UUID id;
    private String title;
    private double price;
    private String category;
    private String description;
    private String image;
    
    // Enhanced fields for search and inventory
    private String brand;
    private Integer stockQuantity;
    private Boolean inStock;
    private ProductStatus status;
    private List<String> tags;
    private String searchKeywords;
    
    // Search-specific metadata
    private Double relevanceScore; // Elasticsearch relevance score
    private Boolean isRecommended; // For recommendation engine
}

