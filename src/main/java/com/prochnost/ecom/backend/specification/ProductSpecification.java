package com.prochnost.ecom.backend.specification;

import com.prochnost.ecom.backend.dto.SearchRequestDTO;
import com.prochnost.ecom.backend.model.Product;
import com.prochnost.ecom.backend.model.ProductStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> createSpecification(SearchRequestDTO searchRequest) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Always filter by active status
            predicates.add(criteriaBuilder.equal(root.get("status"), ProductStatus.ACTIVE));

            // General query search (title, description, keywords)
            if (searchRequest.getQuery() != null && !searchRequest.getQuery().trim().isEmpty()) {
                String searchPattern = "%" + searchRequest.getQuery().toLowerCase() + "%";
                Predicate titlePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")), searchPattern);
                Predicate descriptionPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")), searchPattern);
                Predicate keywordsPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("searchKeywords")), searchPattern);
                
                predicates.add(criteriaBuilder.or(titlePredicate, descriptionPredicate, keywordsPredicate));
            }

            // Specific title search
            if (searchRequest.getTitle() != null && !searchRequest.getTitle().trim().isEmpty()) {
                if (searchRequest.getExactMatch()) {
                    predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("title")), 
                        searchRequest.getTitle().toLowerCase()));
                } else {
                    predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")), 
                        "%" + searchRequest.getTitle().toLowerCase() + "%"));
                }
            }

            // Description search
            if (searchRequest.getDescription() != null && !searchRequest.getDescription().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")), 
                    "%" + searchRequest.getDescription().toLowerCase() + "%"));
            }

            // Category filter
            if (searchRequest.getCategory() != null && !searchRequest.getCategory().trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("category").get("categoryName")), 
                    searchRequest.getCategory().toLowerCase()));
            }

            // Multiple categories filter
            if (searchRequest.getCategories() != null && !searchRequest.getCategories().isEmpty()) {
                List<String> lowerCaseCategories = searchRequest.getCategories().stream()
                    .map(String::toLowerCase)
                    .toList();
                predicates.add(criteriaBuilder.lower(root.get("category").get("categoryName"))
                    .in(lowerCaseCategories));
            }

            // Price range filter
            if (searchRequest.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("price").get("amount"), searchRequest.getMinPrice()));
            }
            if (searchRequest.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("price").get("amount"), searchRequest.getMaxPrice()));
            }

            // Stock filter
            if (searchRequest.getInStock() != null) {
                if (searchRequest.getInStock()) {
                    predicates.add(criteriaBuilder.isTrue(root.get("inStock")));
                    predicates.add(criteriaBuilder.greaterThan(root.get("stockQuantity"), 0));
                } else if (!searchRequest.getIncludeOutOfStock()) {
                    predicates.add(criteriaBuilder.isTrue(root.get("inStock")));
                    predicates.add(criteriaBuilder.greaterThan(root.get("stockQuantity"), 0));
                }
            }

            // Brand filter
            if (searchRequest.getBrand() != null && !searchRequest.getBrand().trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("brand")), 
                    searchRequest.getBrand().toLowerCase()));
            }

            // Tags filter
            if (searchRequest.getTags() != null && !searchRequest.getTags().isEmpty()) {
                // For tags (ElementCollection), we need to join
                predicates.add(root.join("tags").in(searchRequest.getTags()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
