package com.prochnost.ecom.backend.service.productService;

import com.prochnost.ecom.backend.dto.productDto.ProductListResponseDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductRequestDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.exceptions.ProductNotFoundException;

import java.util.UUID;

public interface ProductService {
    ProductListResponseDTO getAllProducts();
    ProductResponseDTO getProductById(UUID id) throws ProductNotFoundException;
    ProductResponseDTO getProductByTitle(String title) throws ProductNotFoundException;
    ProductListResponseDTO getProductsByCategory(String categoryName);
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
    boolean deleteProduct(UUID id);
    boolean updateProduct(UUID id, ProductRequestDTO updatedProduct) throws  ProductNotFoundException;

}
