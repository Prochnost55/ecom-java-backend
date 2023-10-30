package com.prochnost.ecom.backend.service;

import com.prochnost.ecom.backend.dto.ProductListResponseDTO;
import com.prochnost.ecom.backend.dto.ProductRequestDTO;
import com.prochnost.ecom.backend.dto.ProductResponseDTO;
import com.prochnost.ecom.backend.exceptions.ProductNotFoundException;
import com.prochnost.ecom.backend.model.Product;
import org.springframework.stereotype.Service;

@Service("productService")
public class ProductServiceImpl implements ProductService{
    @Override
    public ProductListResponseDTO getAllProducts() {
        return null;
    }

    @Override
    public ProductResponseDTO getProductById(int id) throws ProductNotFoundException {
        return null;
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO product) {
        return null;
    }

    @Override
    public boolean deleteProduct(int id) {
        return false;
    }

    @Override
    public Product updateProduct(int id, Product updatedProduct) {
        return null;
    }
}
