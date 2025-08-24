package com.prochnost.ecom.backend.dto;

import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchResponseDTO {
    private List<ProductResponseDTO> products;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
    
    // Search metadata
    private String searchQuery;
    private long searchTimeMs;
    private List<String> appliedFilters;
    private List<String> availableCategories;
    private Double minPriceFound;
    private Double maxPriceFound;
    
    // Search suggestions (for future autocomplete)
    private List<String> suggestions;
    
    public SearchResponseDTO() {
        // Default constructor
    }
    
    public SearchResponseDTO(List<ProductResponseDTO> products, long totalElements, 
                           int currentPage, int pageSize) {
        this.products = products;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        this.hasNext = currentPage < totalPages - 1;
        this.hasPrevious = currentPage > 0;
    }
}
