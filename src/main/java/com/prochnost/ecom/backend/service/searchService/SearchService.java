package com.prochnost.ecom.backend.service.searchService;

import com.prochnost.ecom.backend.dto.SearchRequestDTO;
import com.prochnost.ecom.backend.dto.SearchResponseDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.mapper.ProductMapper;
import com.prochnost.ecom.backend.model.Product;
import com.prochnost.ecom.backend.model.ProductStatus;
import com.prochnost.ecom.backend.model.SortParams;
import com.prochnost.ecom.backend.repository.ProductRepository;
import com.prochnost.ecom.backend.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class SearchService {
    
    @Autowired
    private ProductRepository productRepository;

    public SearchService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    
    // Enhanced search method returning SearchResponseDTO
    public SearchResponseDTO searchProducts(SearchRequestDTO searchRequest) {
        long startTime = System.currentTimeMillis();
        
        // Build sort and pagination
        Sort sort = buildSort(searchRequest);
        PageRequest pageRequest = PageRequest.of(
            searchRequest.getPageNumber(), 
            searchRequest.getItemsPerPage(), 
            sort != null ? sort : Sort.by("title").ascending()
        );
        
        // Use enhanced specification
        Specification<Product> spec = ProductSpecification.createSpecification(searchRequest);
        Page<Product> productPage = productRepository.findAll(spec, pageRequest);
        
        // Convert to DTOs
        List<ProductResponseDTO> productDTOs = productPage.getContent().stream()
            .map(ProductMapper::productToProductResponseDTO)
            .collect(Collectors.toList());
        
        // Build response with metadata
        SearchResponseDTO response = new SearchResponseDTO(
            productDTOs,
            productPage.getTotalElements(),
            productPage.getNumber(),
            productPage.getSize()
        );
        
        // Add search metadata
        response.setSearchQuery(searchRequest.getQuery());
        response.setSearchTimeMs(System.currentTimeMillis() - startTime);
        response.setAppliedFilters(buildAppliedFiltersList(searchRequest));
        response.setAvailableCategories(getAvailableCategories());
        response.setMinPriceFound(getMinPrice());
        response.setMaxPriceFound(getMaxPrice());
        
        return response;
    }
    
    // Legacy method for backward compatibility
    public List<ProductResponseDTO> searchProduct(SearchRequestDTO searchRequest){
        return searchProducts(searchRequest).getProducts();
    }
    
    // Get search suggestions for autocomplete
    public List<String> getSearchSuggestions(String query) {
        if (query == null || query.trim().length() < 2) {
            return new ArrayList<>();
        }
        return productRepository.findSearchSuggestions(query.trim());
    }
    
    // Get all available categories
    public List<String> getAvailableCategories() {
        return productRepository.findAllActiveCategories();
    }
    
    // Get all available brands
    public List<String> getAvailableBrands() {
        return productRepository.findAllActiveBrands();
    }
    
    // Get filter metadata (price ranges, categories, brands)
    public SearchResponseDTO getFilterMetadata() {
        SearchResponseDTO metadata = new SearchResponseDTO();
        metadata.setAvailableCategories(getAvailableCategories());
        metadata.setMinPriceFound(getMinPrice());
        metadata.setMaxPriceFound(getMaxPrice());
        metadata.setSuggestions(getAvailableBrands()); // Reusing suggestions field for brands
        return metadata;
    }
    
    private Double getMinPrice() {
        return productRepository.findMinPrice();
    }
    
    private Double getMaxPrice() {
        return productRepository.findMaxPrice();
    }
    
    private List<String> buildAppliedFiltersList(SearchRequestDTO searchRequest) {
        List<String> filters = new ArrayList<>();
        
        if (searchRequest.getQuery() != null && !searchRequest.getQuery().trim().isEmpty()) {
            filters.add("Query: " + searchRequest.getQuery());
        }
        if (searchRequest.getCategory() != null && !searchRequest.getCategory().trim().isEmpty()) {
            filters.add("Category: " + searchRequest.getCategory());
        }
        if (searchRequest.getMinPrice() != null) {
            filters.add("Min Price: $" + searchRequest.getMinPrice());
        }
        if (searchRequest.getMaxPrice() != null) {
            filters.add("Max Price: $" + searchRequest.getMaxPrice());
        }
        if (searchRequest.getBrand() != null && !searchRequest.getBrand().trim().isEmpty()) {
            filters.add("Brand: " + searchRequest.getBrand());
        }
        if (searchRequest.getInStock() != null && searchRequest.getInStock()) {
            filters.add("In Stock Only");
        }
        
        return filters;
    }
    
    private Sort buildSort(SearchRequestDTO searchRequest) {
        // Priority: sortBy list > individual sort fields > default
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            return buildSortFromList(searchRequest.getSortBy());
        }
        
        if (searchRequest.getSortField() != null && searchRequest.getSortDirection() != null) {
            return "DESC".equalsIgnoreCase(searchRequest.getSortDirection()) 
                ? Sort.by(searchRequest.getSortField()).descending()
                : Sort.by(searchRequest.getSortField()).ascending();
        }
        
        return Sort.by("title").ascending(); // Default sort
    }
    
    private Sort buildSortFromList(List<SortParams> sortBy) {
        if (isNull(sortBy) || sortBy.isEmpty()) {
            return null;
        }
        
        Sort sort = null;
        if (sortBy.get(0).getOrder().equals("ASC")) {
            sort = Sort.by(sortBy.get(0).getParamName()).ascending();
        } else {
            sort = Sort.by(sortBy.get(0).getParamName()).descending();
        }
        
        for (int i = 1; i < sortBy.size(); i++) {
            if (sortBy.get(i).getOrder().equals("ASC")) {
                sort = sort.and(Sort.by(sortBy.get(i).getParamName()).ascending());
            } else {
                sort = sort.and(Sort.by(sortBy.get(i).getParamName()).descending());
            }
        }
        return sort;
    }
}
