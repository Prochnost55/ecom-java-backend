package com.prochnost.ecom.backend.client;

import com.prochnost.ecom.backend.dto.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class FakeStoreAPIClient {
    private RestTemplateBuilder restTemplateBuilder;

    public FakeStoreAPIClient(RestTemplateBuilder restTemplateBuilder){
        this.restTemplateBuilder = restTemplateBuilder;
    }

    public List<FakeStoreProductResponseDTO> getAllProducts(){
        String getAllProducts = "https://fakestoreapi.com/products";
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductResponseDTO[]> products = restTemplate.getForEntity(getAllProducts, FakeStoreProductResponseDTO[].class);
        return List.of(products.getBody());
    }

    public FakeStoreProductResponseDTO getProduct(int id){
        String getProductByIdAPI = "https://fakestoreapi.com/products/"+id;
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductResponseDTO> productResponse =
                restTemplate.getForEntity(getProductByIdAPI, FakeStoreProductResponseDTO.class);
        return productResponse.getBody();
    }

    public FakeStoreProductResponseDTO createProduct(FakeStoreProductRequestDTO fakeStoreProductRequestDTO){
        String postProductAPI = "https://fakestoreapi.com/products";
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductResponseDTO> response = restTemplate.postForEntity(postProductAPI,fakeStoreProductRequestDTO, FakeStoreProductResponseDTO.class);
        return response.getBody();
    }

    public boolean deleteProduct(int id){
        String deleteProductAPI = "https://fakestoreapi.com/products/"+id;
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.delete(deleteProductAPI);
        return true;
    }
}
