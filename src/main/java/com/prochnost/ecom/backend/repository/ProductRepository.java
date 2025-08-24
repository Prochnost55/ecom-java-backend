package com.prochnost.ecom.backend.repository;

import com.prochnost.ecom.backend.model.Product;
import com.prochnost.ecom.backend.model.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    
    // Basic queries
    Product getProductByTitle(String title);
    List<Product> findByTitleLike(String title);
    List<Product> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);
    List<Product> findByCategoryCategoryNameIgnoreCase(String categoryName);
    
    // Enhanced search queries
    @Query("SELECT p FROM Product p WHERE " +
           "(:query IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.searchKeywords) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:category IS NULL OR LOWER(p.category.categoryName) = LOWER(:category)) AND " +
           "(:minPrice IS NULL OR p.price.amount >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price.amount <= :maxPrice) AND " +
           "(:inStock IS NULL OR p.inStock = :inStock) AND " +
           "(:brand IS NULL OR LOWER(p.brand) = LOWER(:brand)) AND " +
           "p.status = :status")
    Page<Product> findWithFilters(
            @Param("query") String query,
            @Param("category") String category,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("inStock") Boolean inStock,
            @Param("brand") String brand,
            @Param("status") ProductStatus status,
            Pageable pageable
    );
    
    // Category-based queries
    @Query("SELECT DISTINCT p.category.categoryName FROM Product p WHERE p.status = 'ACTIVE'")
    List<String> findAllActiveCategories();
    
    // Price range queries
    @Query("SELECT MIN(p.price.amount) FROM Product p WHERE p.status = 'ACTIVE'")
    Double findMinPrice();
    
    @Query("SELECT MAX(p.price.amount) FROM Product p WHERE p.status = 'ACTIVE'")
    Double findMaxPrice();
    
    // Brand queries
    @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.brand IS NOT NULL AND p.status = 'ACTIVE' ORDER BY p.brand")
    List<String> findAllActiveBrands();
    
    // Stock queries
    List<Product> findByInStockTrue();
    List<Product> findByStockQuantityLessThan(Integer threshold);
    
    // Search suggestions
    @Query("SELECT DISTINCT p.title FROM Product p WHERE " +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) AND " +
           "p.status = 'ACTIVE' ORDER BY p.title LIMIT 10")
    List<String> findSearchSuggestions(@Param("query") String query);
    
    // Advanced text search
    @Query("SELECT p FROM Product p WHERE " +
           "MATCH(p.title, p.description, p.searchKeywords) AGAINST(:searchTerm IN NATURAL LANGUAGE MODE) AND " +
           "p.status = 'ACTIVE'")
    List<Product> findByFullTextSearch(@Param("searchTerm") String searchTerm);
}