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
    private String title;
    private String category;
    private Double minPrice;
    private Double maxPrice;
    
    @NotNull(message = "Page number cannot be null")
    @Min(value = 0, message = "Page number must be non-negative")
    private int pageNumber;
    
    @NotNull(message = "Items per page cannot be null")
    @Min(value = 1, message = "Items per page must be at least 1")
    private int itemsPerPage;
    
    private List<SortParams> sortBy;
}