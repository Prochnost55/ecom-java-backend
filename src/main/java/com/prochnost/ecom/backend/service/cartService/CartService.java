package com.prochnost.ecom.backend.service.cartService;

import com.prochnost.ecom.backend.dto.cartDto.AddToCartRequestDTO;
import com.prochnost.ecom.backend.dto.cartDto.CartResponseDTO;
import com.prochnost.ecom.backend.dto.cartDto.CheckoutRequestDTO;
import com.prochnost.ecom.backend.dto.cartDto.UpdateCartItemRequestDTO;
import com.prochnost.ecom.backend.dto.orderDto.OrderResponseDTO;
import com.prochnost.ecom.backend.exceptions.CartNotFoundException;
import com.prochnost.ecom.backend.exceptions.ProductNotFoundException;

import java.util.UUID;

public interface CartService {
    
    // Add item to cart
    CartResponseDTO addToCart(AddToCartRequestDTO addToCartRequestDTO) throws ProductNotFoundException;
    
    // Get user's active cart
    CartResponseDTO getUserCart(Long userId) throws CartNotFoundException;
    
    // Update cart item quantity
    CartResponseDTO updateCartItem(UpdateCartItemRequestDTO updateCartItemRequestDTO) throws CartNotFoundException;
    
    // Remove item from cart
    CartResponseDTO removeFromCart(UUID cartItemId, Long userId) throws CartNotFoundException;
    
    // Clear entire cart
    boolean clearCart(Long userId) throws CartNotFoundException;
    
    // Checkout - convert cart to order
    OrderResponseDTO checkout(CheckoutRequestDTO checkoutRequestDTO) throws CartNotFoundException;
    
    // Get cart by ID
    CartResponseDTO getCartById(UUID cartId) throws CartNotFoundException;
}
