package com.prochnost.ecom.backend.mapper;

import com.prochnost.ecom.backend.dto.cartDto.CartItemResponseDTO;
import com.prochnost.ecom.backend.dto.cartDto.CartResponseDTO;
import com.prochnost.ecom.backend.model.Cart;
import com.prochnost.ecom.backend.model.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {
    
    public static CartResponseDTO cartToCartResponseDTO(Cart cart) {
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setId(cart.getId());
        cartResponseDTO.setUserId(cart.getUserId());
        cartResponseDTO.setTotalAmount(cart.getTotalAmount());
        cartResponseDTO.setTotalItems(cart.getTotalItems());
        cartResponseDTO.setStatus(cart.getStatus());
        cartResponseDTO.setCreatedAt(cart.getCreatedAt());
        cartResponseDTO.setUpdatedAt(cart.getUpdatedAt());
        
        // Map cart items
        List<CartItemResponseDTO> cartItemResponseDTOs = cart.getCartItems().stream()
                .map(CartMapper::cartItemToCartItemResponseDTO)
                .collect(Collectors.toList());
        cartResponseDTO.setCartItems(cartItemResponseDTOs);
        
        return cartResponseDTO;
    }
    
    public static CartItemResponseDTO cartItemToCartItemResponseDTO(CartItem cartItem) {
        CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
        cartItemResponseDTO.setId(cartItem.getId());
        cartItemResponseDTO.setQuantity(cartItem.getQuantity());
        cartItemResponseDTO.setUnitPrice(cartItem.getUnitPrice());
        cartItemResponseDTO.setItemTotal(cartItem.getItemTotal());
        
        // Map product
        cartItemResponseDTO.setProduct(ProductMapper.productToProductResponseDTO(cartItem.getProduct()));
        
        return cartItemResponseDTO;
    }
}
