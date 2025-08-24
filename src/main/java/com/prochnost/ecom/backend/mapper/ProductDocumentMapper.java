package com.prochnost.ecom.backend.mapper;

import com.prochnost.ecom.backend.document.ProductDocument;
import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.model.Product;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductDocumentMapper {

    public static ProductDocument toDocument(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductDocument(
                product.getId().toString(),
                product.getTitle(),
                product.getDescription(),
                product.getCategory() != null ? product.getCategory().getCategoryName() : null,
                product.getBrand(),
                product.getPrice() != null ? product.getPrice().getAmount() : null,
                product.getImage(),
                product.getStockQuantity(),
                product.getInStock(),
                product.getStatus(),
                product.getSearchKeywords(),
                product.getTags()
        );
    }

    public static ProductResponseDTO documentToResponseDTO(ProductDocument document) {
        if (document == null) {
            return null;
        }

        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(java.util.UUID.fromString(document.getId()));
        dto.setTitle(document.getTitle());
        dto.setDescription(document.getDescription());
        dto.setCategory(document.getCategory());
        dto.setPrice(document.getPrice());
        dto.setImage(document.getImage());
        
        // Add new fields
        dto.setBrand(document.getBrand());
        dto.setStockQuantity(document.getStockQuantity());
        dto.setInStock(document.getInStock());
        dto.setStatus(document.getStatus());
        dto.setTags(document.getTags());

        return dto;
    }

    public static Product documentToProduct(ProductDocument document) {
        if (document == null) {
            return null;
        }
        
        // This is a simplified conversion - in real scenarios you'd need
        // to handle relationships properly (Category, Price entities)
        Product product = new Product();
        product.setId(java.util.UUID.fromString(document.getId()));
        product.setTitle(document.getTitle());
        product.setDescription(document.getDescription());
        product.setBrand(document.getBrand());
        product.setImage(document.getImage());
        product.setStockQuantity(document.getStockQuantity());
        product.setInStock(document.getInStock());
        product.setStatus(document.getStatus());
        product.setSearchKeywords(document.getSearchKeywords());
        product.setTags(document.getTags());
        
        return product;
    }
}
