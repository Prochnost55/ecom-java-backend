package com.prochnost.ecom.backend.document;

import com.prochnost.ecom.backend.model.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "products")
@Setting(replicas = 0, shards = 1)
public class ProductDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Keyword)
    private String brand;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Text)
    private String image;

    @Field(type = FieldType.Integer)
    private Integer stockQuantity;

    @Field(type = FieldType.Boolean)
    private Boolean inStock;

    @Field(type = FieldType.Keyword)
    private ProductStatus status;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String searchKeywords;

    @Field(type = FieldType.Keyword)
    private List<String> tags;

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date)
    private LocalDateTime updatedAt;

    // Search-specific fields
    @Field(type = FieldType.Text, analyzer = "keyword")
    private String titleExact; // For exact matches

    @Field(type = FieldType.Text, analyzer = "standard")
    private String allText; // Combined text for full-text search

    @Field(type = FieldType.Double)
    private Double popularityScore; // For relevance boosting

    // Constructor from Product entity
    public ProductDocument(String id, String title, String description, String category, 
                          String brand, Double price, String image, Integer stockQuantity, 
                          Boolean inStock, ProductStatus status, String searchKeywords, 
                          List<String> tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.brand = brand;
        this.price = price;
        this.image = image;
        this.stockQuantity = stockQuantity;
        this.inStock = inStock;
        this.status = status;
        this.searchKeywords = searchKeywords;
        this.tags = tags;
        this.titleExact = title;
        this.allText = buildAllText();
        this.popularityScore = 1.0; // Default score
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private String buildAllText() {
        StringBuilder sb = new StringBuilder();
        if (title != null) sb.append(title).append(" ");
        if (description != null) sb.append(description).append(" ");
        if (brand != null) sb.append(brand).append(" ");
        if (category != null) sb.append(category).append(" ");
        if (searchKeywords != null) sb.append(searchKeywords).append(" ");
        if (tags != null) {
            tags.forEach(tag -> sb.append(tag).append(" "));
        }
        return sb.toString().trim();
    }

    // Helper method to check if product is available for search
    public boolean isSearchable() {
        return status == ProductStatus.ACTIVE && inStock && stockQuantity > 0;
    }
}
