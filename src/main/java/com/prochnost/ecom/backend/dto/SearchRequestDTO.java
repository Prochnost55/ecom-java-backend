package com.prochnost.ecom.backend.dto;

import com.prochnost.ecom.backend.model.SortParams;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class SearchRequestDTO {
    // Text search
    private String query; // General search query
    private String title; // Specific title search
    private String description; // Description search
    
    // Category filtering
    private String category;
    private List<String> categories; // Multiple categories
    
    // Price filtering
    private Double minPrice;
    private Double maxPrice;
    
    // Advanced filters
    private Boolean inStock; // Filter by availability
    private List<String> tags; // Product tags
    private String brand; // Brand filtering
    
    // Search behavior
    private Boolean exactMatch = false; // Exact vs fuzzy search
    private Boolean includeOutOfStock = true; // Include out of stock products
    
    // Pagination
    @NotNull(message = "Page number cannot be null")
    @Min(value = 0, message = "Page number must be non-negative")
    private int pageNumber = 0;
    
    @NotNull(message = "Items per page cannot be null")
    @Min(value = 1, message = "Items per page must be at least 1")
    private int itemsPerPage = 20;
    
    // Sorting
    private List<SortParams> sortBy;
    private String sortField = "title"; // Default sort field
    private String sortDirection = "ASC"; // ASC or DESC
}