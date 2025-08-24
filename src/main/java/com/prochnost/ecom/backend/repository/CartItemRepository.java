package com.prochnost.ecom.backend.repository;

import com.prochnost.ecom.backend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    
    // Find cart item by cart and product
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.product.id = :productId")
    Optional<CartItem> findByCartIdAndProductId(@Param("cartId") UUID cartId, @Param("productId") UUID productId);
    
    // Find all items in a cart
    List<CartItem> findByCartId(UUID cartId);
    
    // Delete all items in a cart
    void deleteByCartId(UUID cartId);
    
    // Check if product exists in cart
    boolean existsByCartIdAndProductId(UUID cartId, UUID productId);
}
