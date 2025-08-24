package com.prochnost.ecom.backend.service.sync;

import com.prochnost.ecom.backend.model.Product;
import com.prochnost.ecom.backend.service.searchService.ElasticsearchSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Service to handle synchronization between MySQL and Elasticsearch
 * This ensures data consistency between the primary database and search index
 */
@Service
public class ProductSyncService {

    @Autowired
    private ElasticsearchSearchService elasticsearchSearchService;

    /**
     * Sync a single product to Elasticsearch after create/update operations
     */
    public void syncProductToElasticsearch(Product product) {
        try {
            elasticsearchSearchService.syncProductToElasticsearch(product);
        } catch (Exception e) {
            // Log error but don't fail the main operation
            System.err.println("Failed to sync product " + product.getId() + " to Elasticsearch: " + e.getMessage());
        }
    }

    /**
     * Remove product from Elasticsearch after delete operations
     */
    public void removeProductFromElasticsearch(String productId) {
        try {
            elasticsearchSearchService.deleteProductFromElasticsearch(productId);
        } catch (Exception e) {
            // Log error but don't fail the main operation
            System.err.println("Failed to remove product " + productId + " from Elasticsearch: " + e.getMessage());
        }
    }

    /**
     * Bulk sync all products to Elasticsearch
     * This is useful for initial setup or data recovery
     */
    public void bulkSyncAllProducts() {
        try {
            elasticsearchSearchService.bulkSyncProductsToElasticsearch();
        } catch (Exception e) {
            System.err.println("Failed to bulk sync products to Elasticsearch: " + e.getMessage());
            throw new RuntimeException("Bulk sync failed", e);
        }
    }

    /**
     * Event listener for product creation/update events
     * This would be triggered by Spring Events if implemented
     */
    // @EventListener
    // public void handleProductChangeEvent(ProductChangeEvent event) {
    //     if (event.getEventType() == ProductChangeEvent.EventType.CREATED ||
    //         event.getEventType() == ProductChangeEvent.EventType.UPDATED) {
    //         syncProductToElasticsearch(event.getProduct());
    //     } else if (event.getEventType() == ProductChangeEvent.EventType.DELETED) {
    //         removeProductFromElasticsearch(event.getProduct().getId().toString());
    //     }
    // }
}
