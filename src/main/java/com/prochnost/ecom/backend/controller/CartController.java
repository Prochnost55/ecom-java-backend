package com.prochnost.ecom.backend.controller;

import com.prochnost.ecom.backend.dto.cartDto.AddToCartRequestDTO;
import com.prochnost.ecom.backend.dto.cartDto.CartResponseDTO;
import com.prochnost.ecom.backend.dto.cartDto.CheckoutRequestDTO;
import com.prochnost.ecom.backend.dto.cartDto.UpdateCartItemRequestDTO;
import com.prochnost.ecom.backend.dto.orderDto.OrderResponseDTO;
import com.prochnost.ecom.backend.exceptions.CartNotFoundException;
import com.prochnost.ecom.backend.exceptions.ProductNotFoundException;
import com.prochnost.ecom.backend.service.cartService.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
@Tag(name = "Shopping Cart Management", description = "APIs for managing user shopping cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Operation(summary = "Add item to cart", description = "Add a product to user's shopping cart")
    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addToCart(@Valid @RequestBody AddToCartRequestDTO addToCartRequestDTO) 
            throws ProductNotFoundException {
        CartResponseDTO response = cartService.addToCart(addToCartRequestDTO);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get user cart", description = "Retrieve user's active shopping cart")
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponseDTO> getUserCart(
            @Parameter(description = "User ID") @PathVariable Long userId) 
            throws CartNotFoundException {
        CartResponseDTO response = cartService.getUserCart(userId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Update cart item", description = "Update quantity of an item in cart")
    @PutMapping("/update")
    public ResponseEntity<CartResponseDTO> updateCartItem(@Valid @RequestBody UpdateCartItemRequestDTO updateCartItemRequestDTO) 
            throws CartNotFoundException {
        CartResponseDTO response = cartService.updateCartItem(updateCartItemRequestDTO);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Remove item from cart", description = "Remove a specific item from user's cart")
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<CartResponseDTO> removeFromCart(
            @Parameter(description = "Cart Item ID") @PathVariable String cartItemId,
            @Parameter(description = "User ID") @RequestParam Long userId) 
            throws CartNotFoundException {
        CartResponseDTO response = cartService.removeFromCart(UUID.fromString(cartItemId), userId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Clear cart", description = "Remove all items from user's cart")
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Boolean> clearCart(
            @Parameter(description = "User ID") @PathVariable Long userId) 
            throws CartNotFoundException {
        boolean response = cartService.clearCart(userId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Checkout", description = "Convert cart to order and initiate checkout process")
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkout(@Valid @RequestBody CheckoutRequestDTO checkoutRequestDTO) 
            throws CartNotFoundException {
        OrderResponseDTO response = cartService.checkout(checkoutRequestDTO);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get cart by ID", description = "Retrieve cart details by cart ID")
    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponseDTO> getCartById(
            @Parameter(description = "Cart ID") @PathVariable String cartId) 
            throws CartNotFoundException {
        CartResponseDTO response = cartService.getCartById(UUID.fromString(cartId));
        return ResponseEntity.ok(response);
    }
}
