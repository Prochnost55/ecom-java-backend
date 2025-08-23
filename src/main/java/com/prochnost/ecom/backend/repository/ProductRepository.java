package com.prochnost.ecom.backend.repository;

import com.prochnost.ecom.backend.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    public Product getProductByTitle(String title); // example of custom query
    // just declare the query correctly and it should fetch correct results
    public List<Product> findByTitleLike(String title); // example of like query
    public List<Product> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);
    public List<Product> findByCategoryCategoryNameIgnoreCase(String categoryName);
}