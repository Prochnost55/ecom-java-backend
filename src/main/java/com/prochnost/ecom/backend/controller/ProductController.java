package com.prochnost.ecom.backend.controller;

import com.prochnost.ecom.backend.dto.productDto.ProductListResponseDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductRequestDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.exceptions.ProductNotFoundException;
import com.prochnost.ecom.backend.service.productService.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping("/products")
@RestController
@Tag(name = "Product Management", description = "APIs for managing products in the e-commerce system")
public class ProductController {

    @Autowired
    @Qualifier("productService")
    private ProductService productService;

    @Operation(summary = "Get all products", description = "Retrieve a list of all products in the system")
    @GetMapping("")
    public ResponseEntity getAllProducts(){
        ProductListResponseDTO response = productService.getAllProducts();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its unique identifier")
    @GetMapping("/{id}")
    public ResponseEntity getProductById(@Parameter(description = "Product ID") @PathVariable String id) throws ProductNotFoundException {
        ProductResponseDTO response = productService.getProductById(UUID.fromString(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity getProductByTitle(@PathVariable String title) throws ProductNotFoundException {
        ProductResponseDTO response = productService.getProductByTitle(title);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity getProductsByCategory(@PathVariable String categoryName) {
        ProductListResponseDTO response = productService.getProductsByCategory(categoryName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO){
        ProductResponseDTO response = productService.createProduct(productRequestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable String id){
        boolean response = productService.deleteProduct(UUID.fromString(id));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateProduct(@PathVariable String id, @Valid @RequestBody ProductRequestDTO productRequestDTO) throws ProductNotFoundException {
        boolean response = productService.updateProduct(UUID.fromString(id),productRequestDTO);
        return ResponseEntity.ok(response);
    }
}
