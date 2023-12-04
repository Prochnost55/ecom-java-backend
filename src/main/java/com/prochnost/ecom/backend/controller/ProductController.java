package com.prochnost.ecom.backend.controller;

import com.prochnost.ecom.backend.dto.productDto.ProductListResponseDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductRequestDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.exceptions.ProductNotFoundException;
import com.prochnost.ecom.backend.service.productService.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/products")
@RestController
public class ProductController {

    @Autowired
    @Qualifier("productService")
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity getAllProducts(){
        ProductListResponseDTO response = productService.getAllProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity getProductById(@PathVariable String id) throws ProductNotFoundException {
        ProductResponseDTO response = productService.getProductById(UUID.fromString(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity getProductByTitle(@PathVariable String title) throws ProductNotFoundException {
        ProductResponseDTO response = productService.getProductByTitle(title);
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity createProduct(@RequestBody ProductRequestDTO productRequestDTO){
        ProductResponseDTO response = productService.createProduct(productRequestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable String id){
        boolean response = productService.deleteProduct(UUID.fromString(id));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateProduct(@PathVariable String id, @RequestBody ProductRequestDTO productRequestDTO) throws ProductNotFoundException {
        boolean response = productService.updateProduct(UUID.fromString(id),productRequestDTO);
        return ResponseEntity.ok(response);
    }
}
