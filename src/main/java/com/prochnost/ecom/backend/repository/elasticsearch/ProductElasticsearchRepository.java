package com.prochnost.ecom.backend.repository.elasticsearch;

import com.prochnost.ecom.backend.document.ProductDocument;
import com.prochnost.ecom.backend.model.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, String> {

    // Basic search queries
    List<ProductDocument> findByTitle(String title);
    List<ProductDocument> findByTitleContainingIgnoreCase(String title);
    List<ProductDocument> findByCategory(String category);
    List<ProductDocument> findByBrand(String brand);
    List<ProductDocument> findByStatus(ProductStatus status);
    List<ProductDocument> findByInStockTrue();

    // Price range queries
    List<ProductDocument> findByPriceBetween(Double minPrice, Double maxPrice);

    // Multi-field search with Elasticsearch query
    @Query("""
        {
          "bool": {
            "must": [
              {
                "multi_match": {
                  "query": "?0",
                  "fields": ["title^3", "description^2", "searchKeywords^2", "allText"],
                  "type": "best_fields",
                  "fuzziness": "AUTO"
                }
              }
            ],
            "filter": [
              {
                "term": {
                  "status": "ACTIVE"
                }
              }
            ]
          }
        }
        """)
    Page<ProductDocument> findByMultiFieldSearch(String query, Pageable pageable);

    // Advanced search with multiple filters
    @Query("""
        {
          "bool": {
            "must": [
              {
                "multi_match": {
                  "query": "?0",
                  "fields": ["title^3", "description^2", "searchKeywords^2", "allText"],
                  "type": "best_fields",
                  "fuzziness": "AUTO"
                }
              }
            ],
            "filter": [
              {
                "term": {
                  "status": "ACTIVE"
                }
              }
            ]
          }
        }
        """)
    Page<ProductDocument> searchWithFilters(String query, Pageable pageable);

    // Category-based search
    @Query("""
        {
          "bool": {
            "must": [
              {
                "multi_match": {
                  "query": "?0",
                  "fields": ["title^3", "description^2", "searchKeywords^2", "allText"],
                  "type": "best_fields",
                  "fuzziness": "AUTO"
                }
              }
            ],
            "filter": [
              {
                "term": {
                  "status": "ACTIVE"
                }
              },
              {
                "term": {
                  "category": "?1"
                }
              }
            ]
          }
        }
        """)
    Page<ProductDocument> searchInCategory(String query, String category, Pageable pageable);

    // Suggestions for autocomplete
    @Query("""
        {
          "bool": {
            "must": [
              {
                "prefix": {
                  "title": "?0"
                }
              }
            ],
            "filter": [
              {
                "term": {
                  "status": "ACTIVE"
                }
              }
            ]
          }
        }
        """)
    List<ProductDocument> findSuggestions(String prefix);

    // Find similar products
    @Query("""
        {
          "more_like_this": {
            "fields": ["title", "description", "category", "tags"],
            "like": [
              {
                "_id": "?0"
              }
            ],
            "min_term_freq": 1,
            "max_query_terms": 12
          }
        }
        """)
    List<ProductDocument> findSimilarProducts(String productId);

    // Aggregation queries for filters
    @Query("""
        {
          "aggs": {
            "categories": {
              "terms": {
                "field": "category",
                "size": 50
              }
            },
            "brands": {
              "terms": {
                "field": "brand",
                "size": 50
              }
            },
            "price_stats": {
              "stats": {
                "field": "price"
              }
            }
          },
          "size": 0
        }
        """)
    List<ProductDocument> getAggregations();
}
