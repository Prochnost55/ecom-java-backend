package com.prochnost.ecom.backend.service.searchService;

import com.prochnost.ecom.backend.document.ProductDocument;
import com.prochnost.ecom.backend.dto.SearchRequestDTO;
import com.prochnost.ecom.backend.dto.SearchResponseDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.mapper.ProductDocumentMapper;
import com.prochnost.ecom.backend.model.Product;
import com.prochnost.ecom.backend.model.ProductStatus;
import com.prochnost.ecom.backend.repository.ProductRepository;
import com.prochnost.ecom.backend.repository.elasticsearch.ProductElasticsearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("elasticsearchSearchService")
public class ElasticsearchSearchService {

    @Autowired
    private ProductElasticsearchRepository elasticsearchRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ProductDocumentMapper documentMapper;

    /**
     * Advanced search using Elasticsearch with comprehensive filtering
     */
    public SearchResponseDTO searchProducts(SearchRequestDTO searchRequest) {
        long startTime = System.currentTimeMillis();

        // Build pagination and sorting
        PageRequest pageRequest = buildPageRequest(searchRequest);
        
        // Perform Elasticsearch search
        Page<ProductDocument> searchResults = performElasticsearchQuery(searchRequest, pageRequest);
        
        // Convert to DTOs with relevance scores
        List<ProductResponseDTO> productDTOs = searchResults.getContent().stream()
            .map(this::convertToProductResponseDTO)
            .collect(Collectors.toList());

        // Build comprehensive response
        SearchResponseDTO response = new SearchResponseDTO(
            productDTOs,
            searchResults.getTotalElements(),
            searchResults.getNumber(),
            searchResults.getSize()
        );

        // Add search metadata
        enrichSearchResponse(response, searchRequest, startTime);

        return response;
    }

    /**
     * Get autocomplete suggestions using Elasticsearch
     */
    public List<String> getSearchSuggestions(String query) {
        if (query == null || query.trim().length() < 2) {
            return new ArrayList<>();
        }

        List<ProductDocument> suggestions = elasticsearchRepository.findSuggestions(query.toLowerCase());
        return suggestions.stream()
            .map(ProductDocument::getTitle)
            .distinct()
            .limit(10)
            .collect(Collectors.toList());
    }

    /**
     * Get similar products using Elasticsearch More Like This query
     */
    public List<ProductResponseDTO> getSimilarProducts(String productId, int limit) {
        List<ProductDocument> similarProducts = elasticsearchRepository.findSimilarProducts(productId);
        return similarProducts.stream()
            .limit(limit)
            .map(this::convertToProductResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Sync product data from MySQL to Elasticsearch
     */
    public void syncProductToElasticsearch(Product product) {
        ProductDocument document = ProductDocumentMapper.toDocument(product);
        elasticsearchRepository.save(document);
    }

    /**
     * Bulk sync all products from MySQL to Elasticsearch
     */
    public void bulkSyncProductsToElasticsearch() {
        List<Product> allProducts = productRepository.findAll();
        List<ProductDocument> documents = allProducts.stream()
            .map(ProductDocumentMapper::toDocument)
            .collect(Collectors.toList());
        
        elasticsearchRepository.saveAll(documents);
    }

    /**
     * Delete product from Elasticsearch index
     */
    public void deleteProductFromElasticsearch(String productId) {
        elasticsearchRepository.deleteById(productId);
    }

    /**
     * Fuzzy search with typo tolerance
     */
    public SearchResponseDTO fuzzySearch(String query, int page, int size) {
        SearchRequestDTO searchRequest = new SearchRequestDTO();
        searchRequest.setQuery(query);
        searchRequest.setPageNumber(page);
        searchRequest.setItemsPerPage(size);
        searchRequest.setExactMatch(false); // Enable fuzzy matching
        
        return searchProducts(searchRequest);
    }

    private Page<ProductDocument> performElasticsearchQuery(SearchRequestDTO searchRequest, PageRequest pageRequest) {
        String query = searchRequest.getQuery();
        
        if (query != null && !query.trim().isEmpty()) {
            // Use category-specific search if category is provided
            if (searchRequest.getCategory() != null && !searchRequest.getCategory().trim().isEmpty()) {
                return elasticsearchRepository.searchInCategory(query, searchRequest.getCategory(), pageRequest);
            } else {
                return elasticsearchRepository.findByMultiFieldSearch(query, pageRequest);
            }
        } else {
            // No query provided, use filter-only search
            return performFilterOnlySearch(searchRequest, pageRequest);
        }
    }

    private Page<ProductDocument> performFilterOnlySearch(SearchRequestDTO searchRequest, PageRequest pageRequest) {
        // Build criteria for filter-only search
        Criteria criteria = new Criteria("status").is(ProductStatus.ACTIVE);
        
        if (searchRequest.getCategory() != null) {
            criteria = criteria.and("category").is(searchRequest.getCategory());
        }
        
        if (searchRequest.getBrand() != null) {
            criteria = criteria.and("brand").is(searchRequest.getBrand());
        }
        
        if (searchRequest.getMinPrice() != null || searchRequest.getMaxPrice() != null) {
            if (searchRequest.getMinPrice() != null && searchRequest.getMaxPrice() != null) {
                criteria = criteria.and("price").between(searchRequest.getMinPrice(), searchRequest.getMaxPrice());
            } else if (searchRequest.getMinPrice() != null) {
                criteria = criteria.and("price").greaterThanEqual(searchRequest.getMinPrice());
            } else {
                criteria = criteria.and("price").lessThanEqual(searchRequest.getMaxPrice());
            }
        }
        
        if (searchRequest.getInStock() != null && searchRequest.getInStock()) {
            criteria = criteria.and("inStock").is(true);
        }

        Query searchQuery = new CriteriaQuery(criteria).setPageable(pageRequest);
        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(searchQuery, ProductDocument.class);
        
        List<ProductDocument> products = searchHits.stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
        
        return new org.springframework.data.domain.PageImpl<>(products, pageRequest, searchHits.getTotalHits());
    }

    private PageRequest buildPageRequest(SearchRequestDTO searchRequest) {
        Sort sort = buildSort(searchRequest);
        return PageRequest.of(
            searchRequest.getPageNumber(),
            searchRequest.getItemsPerPage(),
            sort != null ? sort : Sort.by("_score").descending() // Default to relevance score
        );
    }

    private Sort buildSort(SearchRequestDTO searchRequest) {
        if (searchRequest.getSortField() != null && searchRequest.getSortDirection() != null) {
            return "DESC".equalsIgnoreCase(searchRequest.getSortDirection())
                ? Sort.by(searchRequest.getSortField()).descending()
                : Sort.by(searchRequest.getSortField()).ascending();
        }
        return null;
    }

    private ProductResponseDTO convertToProductResponseDTO(ProductDocument document) {
        ProductResponseDTO dto = ProductDocumentMapper.documentToResponseDTO(document);
        // Add search-specific metadata
        dto.setRelevanceScore(document.getPopularityScore());
        return dto;
    }

    private void enrichSearchResponse(SearchResponseDTO response, SearchRequestDTO searchRequest, long startTime) {
        response.setSearchQuery(searchRequest.getQuery());
        response.setSearchTimeMs(System.currentTimeMillis() - startTime);
        response.setAppliedFilters(buildAppliedFiltersList(searchRequest));
        
        // Get aggregations from Elasticsearch for filter metadata
        response.setAvailableCategories(getAvailableCategories());
        // Note: For price ranges and brands, you'd typically use Elasticsearch aggregations
        // This is simplified for the example
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

    private List<String> getAvailableCategories() {
        // This would typically use Elasticsearch aggregations
        // For now, fall back to database query
        return productRepository.findAllActiveCategories();
    }
}
