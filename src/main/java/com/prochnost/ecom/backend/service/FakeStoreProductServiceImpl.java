package com.prochnost.ecom.backend.service;

import com.prochnost.ecom.backend.dto.ProductListResponseDTO;
import com.prochnost.ecom.backend.dto.ProductRequestDTO;
import com.prochnost.ecom.backend.dto.ProductResponseDTO;
import com.prochnost.ecom.backend.model.Product;
import jakarta.websocket.server.PathParam;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("fakeStoreProductService")
public class FakeStoreProductServiceImpl implements ProductService {
    private RestTemplateBuilder restTemplateBuilder;

    public FakeStoreProductServiceImpl(RestTemplateBuilder restTemplateBuilder){
        this.restTemplateBuilder = restTemplateBuilder;
    }
    @Override
    public ProductListResponseDTO getAllProducts() {
        String getAllProducts = "https://fakestoreapi.com/products";
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<ProductResponseDTO[]> productResponse = restTemplate.getForEntity(getAllProducts, ProductResponseDTO[].class);

        ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();
        for(ProductResponseDTO res : productResponse.getBody()){
            productListResponseDTO.getProductList().add(res);
        }
        return productListResponseDTO;
    }

    @Override
    public ProductResponseDTO getProductById(int id) {
        String getProductByIdAPI = "https://fakestoreapi.com/products/1";
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<ProductResponseDTO> productResponse =
                restTemplate.getForEntity(getProductByIdAPI, ProductResponseDTO.class);
        return productResponse.getBody();
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        String postProductAPI = "https://fakestoreapi.com/products";
        RestTemplate restTemplate = restTemplateBuilder.build();

        ResponseEntity<ProductResponseDTO> response = restTemplate.postForEntity(postProductAPI,productRequestDTO, ProductResponseDTO.class);

        return response.getBody();
    }

    @Override
    public boolean deleteProduct(int id) {
        String deleteProductAPI = "https://fakestoreapi.com/products/"+id;
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.delete(deleteProductAPI);
        return true;
    }

    @Override
    public Product updateProduct(int id, Product updatedProduct) {
        return null;
    }
}
