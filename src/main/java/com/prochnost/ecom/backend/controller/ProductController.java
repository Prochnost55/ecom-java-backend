package com.prochnost.ecom.backend.controller;

import com.prochnost.ecom.backend.dto.ProductListResponseDTO;
import com.prochnost.ecom.backend.dto.ProductRequestDTO;
import com.prochnost.ecom.backend.dto.ProductResponseDTO;
import com.prochnost.ecom.backend.service.ProductService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    @Qualifier("fakeStoreProductService")
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity getAllProducts(){
        /*
        ProductResponseDTO p1 = new ProductResponseDTO();
        p1.setId(1);
        p1.setCategory("Electronics");
        p1.setTitle("Iphone");
        p1.setPrice(80000);
        p1.setDescription("apple ka phone");
        p1.setImage("https://picsum.photos/200");

        ProductResponseDTO p2 = new ProductResponseDTO();
        p2.setId(2);
        p2.setCategory("Electronics");
        p2.setTitle("Iqoo");
        p2.setPrice(40000);
        p2.setDescription("iqoo ka phone");
        p2.setImage("https://picsum.photos/200");

        List<ProductResponseDTO> products = Arrays.asList(p1, p2);

        return ResponseEntity.ok(products);
         */
        ProductListResponseDTO response = productService.getAllProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity getProductById(@PathVariable int id){

        ProductResponseDTO response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/products")
    public ResponseEntity createProduct(@RequestBody ProductRequestDTO productRequestDTO){
        ProductResponseDTO response = productService.createProduct(productRequestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity deleteProduct(@PathVariable int id){
        boolean response = productService.deleteProduct(id);
        return ResponseEntity.ok(response);
    }
}
