package com.prochnost.ecom.backend.controller;

import com.prochnost.ecom.backend.dto.SearchRequestDTO;
import com.prochnost.ecom.backend.dto.SearchResponseDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.service.searchService.ElasticsearchSearchService;
import com.prochnost.ecom.backend.service.searchService.SearchService;
import com.prochnost.ecom.backend.service.sync.ProductSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/search")
@Tag(name = "Enhanced Product Search", description = "Elasticsearch-powered advanced search and filtering")
public class SearchController {
    
    @Autowired
    private SearchService searchService; // Legacy JPA-based search
    
    @Autowired
    private ElasticsearchSearchService elasticsearchSearchService; // New Elasticsearch-based search
    
    @Autowired
    private ProductSyncService productSyncService;
    
    public SearchController(SearchService searchService, ElasticsearchSearchService elasticsearchSearchService) {
        this.searchService = searchService;
        this.elasticsearchSearchService = elasticsearchSearchService;
    }
    
    @Operation(summary = "Elasticsearch Advanced Search", 
               description = "High-performance search with fuzzy matching, relevance scoring, and advanced filtering")
    @PostMapping("/elasticsearch")
    public ResponseEntity<SearchResponseDTO> elasticsearchSearch(@Valid @RequestBody SearchRequestDTO searchRequestDTO) {
        SearchResponseDTO response = elasticsearchSearchService.searchProducts(searchRequestDTO);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Fuzzy Search", 
               description = "Typo-tolerant search using Elasticsearch fuzzy matching")
    @GetMapping("/fuzzy")
    public ResponseEntity<SearchResponseDTO> fuzzySearch(
            @Parameter(description = "Search query (typos allowed)") @RequestParam String q,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        SearchResponseDTO response = elasticsearchSearchService.fuzzySearch(q, page, size);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get Similar Products", 
               description = "Find products similar to a given product using Elasticsearch More Like This")
    @GetMapping("/similar/{productId}")
    public ResponseEntity<List<ProductResponseDTO>> getSimilarProducts(
            @Parameter(description = "Product ID to find similar products") @PathVariable String productId,
            @Parameter(description = "Maximum number of similar products") @RequestParam(defaultValue = "5") int limit) {
        
        List<ProductResponseDTO> similarProducts = elasticsearchSearchService.getSimilarProducts(productId, limit);
        return ResponseEntity.ok(similarProducts);
    }
    
    @Operation(summary = "Elasticsearch Autocomplete", 
               description = "Fast autocomplete suggestions using Elasticsearch prefix queries")
    @GetMapping("/elasticsearch/suggestions")
    public ResponseEntity<List<String>> getElasticsearchSuggestions(
            @Parameter(description = "Search query for suggestions") @RequestParam String query) {
        List<String> suggestions = elasticsearchSearchService.getSearchSuggestions(query);
        return ResponseEntity.ok(suggestions);
    }
    
    // Admin endpoints for data synchronization
    @Operation(summary = "Bulk Sync to Elasticsearch", 
               description = "Sync all products from MySQL to Elasticsearch (Admin only)")
    @PostMapping("/admin/sync-all")
    public ResponseEntity<String> bulkSyncToElasticsearch() {
        try {
            productSyncService.bulkSyncAllProducts();
            return ResponseEntity.ok("Successfully synced all products to Elasticsearch");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Sync failed: " + e.getMessage());
        }
    }
    
    // Legacy endpoints for backward compatibility
    @Operation(summary = "Advanced Product Search (Legacy)", 
               description = "JPA-based search with comprehensive filtering, sorting, and pagination")
    @PostMapping("/products")
    public ResponseEntity<SearchResponseDTO> searchProducts(@Valid @RequestBody SearchRequestDTO searchRequestDTO) {
        SearchResponseDTO response = searchService.searchProducts(searchRequestDTO);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Legacy Product Search", 
               description = "Legacy endpoint for backward compatibility - returns Page format")
    @PostMapping
    public Page<ProductResponseDTO> searchProduct(@Valid @RequestBody SearchRequestDTO searchRequestDTO){
        List<ProductResponseDTO> productResponseDTO = searchService.searchProduct(searchRequestDTO);
        Page<ProductResponseDTO> productResponseDTOPage = new PageImpl<>(
                productResponseDTO,
                org.springframework.data.domain.PageRequest.of(
                    searchRequestDTO.getPageNumber(), 
                    searchRequestDTO.getItemsPerPage()
                ),
                productResponseDTO.size()
        );
        return productResponseDTOPage;
    }
    
    @Operation(summary = "Get Search Suggestions (Legacy)", 
               description = "JPA-based autocomplete suggestions for search queries")
    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getSearchSuggestions(
            @Parameter(description = "Search query for suggestions") 
            @RequestParam String query) {
        List<String> suggestions = searchService.getSearchSuggestions(query);
        return ResponseEntity.ok(suggestions);
    }
    
    @Operation(summary = "Get Available Categories", 
               description = "Get all available product categories for filtering")
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAvailableCategories() {
        List<String> categories = searchService.getAvailableCategories();
        return ResponseEntity.ok(categories);
    }
    
    @Operation(summary = "Get Available Brands", 
               description = "Get all available product brands for filtering")
    @GetMapping("/brands")
    public ResponseEntity<List<String>> getAvailableBrands() {
        List<String> brands = searchService.getAvailableBrands();
        return ResponseEntity.ok(brands);
    }
    
    @Operation(summary = "Get Filter Metadata", 
               description = "Get metadata for filters including price ranges, categories, and brands")
    @GetMapping("/filters")
    public ResponseEntity<SearchResponseDTO> getFilterMetadata() {
        SearchResponseDTO metadata = searchService.getFilterMetadata();
        return ResponseEntity.ok(metadata);
    }
    
    @Operation(summary = "Quick Search", 
               description = "Quick search with minimal parameters for simple queries")
    @GetMapping("/quick")
    public ResponseEntity<SearchResponseDTO> quickSearch(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Category filter") @RequestParam(required = false) String category,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "title") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "ASC") String direction,
            @Parameter(description = "Use Elasticsearch") @RequestParam(defaultValue = "true") boolean useElasticsearch) {
        
        SearchRequestDTO searchRequest = new SearchRequestDTO();
        searchRequest.setQuery(q);
        searchRequest.setCategory(category);
        searchRequest.setPageNumber(page);
        searchRequest.setItemsPerPage(size);
        searchRequest.setSortField(sort);
        searchRequest.setSortDirection(direction);
        
        SearchResponseDTO response;
        if (useElasticsearch) {
            response = elasticsearchSearchService.searchProducts(searchRequest);
        } else {
            response = searchService.searchProducts(searchRequest);
        }
        
        return ResponseEntity.ok(response);
    }
}
