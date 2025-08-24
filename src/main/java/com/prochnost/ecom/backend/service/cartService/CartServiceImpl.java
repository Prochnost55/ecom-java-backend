package com.prochnost.ecom.backend.service.cartService;

import com.prochnost.ecom.backend.dto.cartDto.AddToCartRequestDTO;
import com.prochnost.ecom.backend.dto.cartDto.CartResponseDTO;
import com.prochnost.ecom.backend.dto.cartDto.CheckoutRequestDTO;
import com.prochnost.ecom.backend.dto.cartDto.UpdateCartItemRequestDTO;
import com.prochnost.ecom.backend.dto.orderDto.OrderResponseDTO;
import com.prochnost.ecom.backend.exceptions.CartNotFoundException;
import com.prochnost.ecom.backend.exceptions.ProductNotFoundException;
import com.prochnost.ecom.backend.mapper.CartMapper;
import com.prochnost.ecom.backend.mapper.OrderMapper;
import com.prochnost.ecom.backend.model.*;
import com.prochnost.ecom.backend.repository.CartItemRepository;
import com.prochnost.ecom.backend.repository.CartRepository;
import com.prochnost.ecom.backend.repository.OrderRepository;
import com.prochnost.ecom.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Override
    public CartResponseDTO addToCart(AddToCartRequestDTO addToCartRequestDTO) throws ProductNotFoundException {
        // Find or create cart for user
        Cart cart = findOrCreateActiveCart(addToCartRequestDTO.getUserId());
        
        // Validate product exists
        Product product = productRepository.findById(addToCartRequestDTO.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + addToCartRequestDTO.getProductId()));
        
        // Check if product already exists in cart
        Optional<CartItem> existingCartItem = cartItemRepository.findByCartIdAndProductId(
                cart.getId(), product.getId());
        
        if (existingCartItem.isPresent()) {
            // Update existing item quantity
            CartItem cartItem = existingCartItem.get();
            cartItem.updateQuantity(cartItem.getQuantity() + addToCartRequestDTO.getQuantity());
            cartItemRepository.save(cartItem);
        } else {
            // Add new item to cart
            CartItem cartItem = new CartItem(product, addToCartRequestDTO.getQuantity());
            cart.addItem(cartItem);
            cartItemRepository.save(cartItem);
        }
        
        // Recalculate and save cart
        cart.calculateTotals();
        Cart savedCart = cartRepository.save(cart);
        
        // Return updated cart
        return CartMapper.cartToCartResponseDTO(savedCart);
    }
    
    @Override
    public CartResponseDTO getUserCart(Long userId) throws CartNotFoundException {
        Cart cart = cartRepository.findByUserIdAndStatusWithItems(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new CartNotFoundException("No active cart found for user: " + userId));
        
        return CartMapper.cartToCartResponseDTO(cart);
    }
    
    @Override
    public CartResponseDTO updateCartItem(UpdateCartItemRequestDTO updateCartItemRequestDTO) throws CartNotFoundException {
        CartItem cartItem = cartItemRepository.findById(updateCartItemRequestDTO.getCartItemId())
                .orElseThrow(() -> new CartNotFoundException("Cart item not found with id: " + updateCartItemRequestDTO.getCartItemId()));
        
        // Update quantity
        cartItem.updateQuantity(updateCartItemRequestDTO.getQuantity());
        cartItemRepository.save(cartItem);
        
        // Recalculate cart totals
        Cart cart = cartItem.getCart();
        cart.calculateTotals();
        Cart savedCart = cartRepository.save(cart);
        
        return CartMapper.cartToCartResponseDTO(savedCart);
    }
    
    @Override
    public CartResponseDTO removeFromCart(UUID cartItemId, Long userId) throws CartNotFoundException {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartNotFoundException("Cart item not found with id: " + cartItemId));
        
        Cart cart = cartItem.getCart();
        
        // Verify cart belongs to user
        if (!cart.getUserId().equals(userId)) {
            throw new CartNotFoundException("Cart item does not belong to user: " + userId);
        }
        
        // Remove item from cart
        cart.removeItem(cartItem);
        cartItemRepository.delete(cartItem);
        
        // Recalculate and save cart
        cart.calculateTotals();
        Cart savedCart = cartRepository.save(cart);
        
        return CartMapper.cartToCartResponseDTO(savedCart);
    }
    
    @Override
    public boolean clearCart(Long userId) throws CartNotFoundException {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new CartNotFoundException("No active cart found for user: " + userId));
        
        // Delete all cart items
        cartItemRepository.deleteByCartId(cart.getId());
        
        // Clear cart items list and recalculate
        cart.getCartItems().clear();
        cart.calculateTotals();
        cartRepository.save(cart);
        
        return true;
    }
    
    @Override
    public OrderResponseDTO checkout(CheckoutRequestDTO checkoutRequestDTO) throws CartNotFoundException {
        // Get cart with items
        Cart cart = cartRepository.findByIdWithItems(checkoutRequestDTO.getCartId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + checkoutRequestDTO.getCartId()));
        
        // Verify cart belongs to user
        if (!cart.getUserId().equals(checkoutRequestDTO.getUserId())) {
            throw new CartNotFoundException("Cart does not belong to user: " + checkoutRequestDTO.getUserId());
        }
        
        // Verify cart is active and has items
        if (cart.getStatus() != CartStatus.ACTIVE || cart.getCartItems().isEmpty()) {
            throw new CartNotFoundException("Cart is not ready for checkout");
        }
        
        // Create order from cart
        Order order = new Order();
        
        // Convert cart items to products list
        List<Product> products = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            // Add product multiple times based on quantity (simple approach)
            for (int i = 0; i < cartItem.getQuantity(); i++) {
                products.add(cartItem.getProduct());
            }
        }
        
        order.setProducts(products);
        order.setPrice(cart.getTotalAmount());
        
        // Save order
        Order savedOrder = orderRepository.save(order);
        
        // Mark cart as converted
        cart.setStatus(CartStatus.CONVERTED);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        
        return OrderMapper.orderToOrderResponseDTO(savedOrder);
    }
    
    @Override
    public CartResponseDTO getCartById(UUID cartId) throws CartNotFoundException {
        Cart cart = cartRepository.findByIdWithItems(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));
        
        return CartMapper.cartToCartResponseDTO(cart);
    }
    
    // Helper method to find or create active cart for user
    private Cart findOrCreateActiveCart(Long userId) {
        Optional<Cart> existingCart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE);
        
        if (existingCart.isPresent()) {
            return existingCart.get();
        }
        
        // Create new cart
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setStatus(CartStatus.ACTIVE);
        newCart.setCreatedAt(LocalDateTime.now());
        newCart.setUpdatedAt(LocalDateTime.now());
        
        return cartRepository.save(newCart);
    }
}
