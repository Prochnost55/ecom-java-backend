package com.prochnost.ecom.backend.repository;

import com.prochnost.ecom.backend.model.Cart;
import com.prochnost.ecom.backend.model.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    
    // Find active cart for a user
    Optional<Cart> findByUserIdAndStatus(Long userId, CartStatus status);
    
    // Find all carts for a user
    List<Cart> findByUserIdOrderByUpdatedAtDesc(Long userId);
    
    // Find abandoned carts (for cleanup or marketing)
    @Query("SELECT c FROM Cart c WHERE c.status = :status AND c.updatedAt < :cutoffDate")
    List<Cart> findAbandonedCarts(@Param("status") CartStatus status, @Param("cutoffDate") LocalDateTime cutoffDate);
    
    // Check if user has any active cart
    boolean existsByUserIdAndStatus(Long userId, CartStatus status);
    
    // Get cart with items (fetch join for performance)
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems ci LEFT JOIN FETCH ci.product p WHERE c.id = :cartId")
    Optional<Cart> findByIdWithItems(@Param("cartId") UUID cartId);
    
    // Find user's active cart with items
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems ci LEFT JOIN FETCH ci.product p WHERE c.userId = :userId AND c.status = :status")
    Optional<Cart> findByUserIdAndStatusWithItems(@Param("userId") Long userId, @Param("status") CartStatus status);
}
