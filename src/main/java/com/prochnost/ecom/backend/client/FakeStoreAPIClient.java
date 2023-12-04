package com.prochnost.ecom.backend.client;

import com.prochnost.ecom.backend.dto.productDto.FakeStoreProductRequestDTO;
import com.prochnost.ecom.backend.dto.productDto.FakeStoreProductResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FakeStoreAPIClient {
    private RestTemplateBuilder restTemplateBuilder;
    private String fakeStoreApiUrl;
    @Value("${fakeStore.api.path.product}")
    private String fakeStoreAPIPathProduct;

    public FakeStoreAPIClient(RestTemplateBuilder restTemplateBuilder, @Value("${fakeStore.api.url}") String fakeStoreApiUrl){
        this.restTemplateBuilder = restTemplateBuilder;
        this.fakeStoreApiUrl = fakeStoreApiUrl;
    }

    public List<FakeStoreProductResponseDTO> getAllProducts(){
        String getAllProducts = fakeStoreApiUrl+fakeStoreAPIPathProduct;
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductResponseDTO[]> products = restTemplate.getForEntity(getAllProducts, FakeStoreProductResponseDTO[].class);
        return List.of(products.getBody());
    }

    public FakeStoreProductResponseDTO getProduct(int id){
        String getProductByIdAPI = fakeStoreApiUrl+fakeStoreAPIPathProduct+"/"+id;
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductResponseDTO> productResponse =
                restTemplate.getForEntity(getProductByIdAPI, FakeStoreProductResponseDTO.class);
        return productResponse.getBody();
    }

    public FakeStoreProductResponseDTO createProduct(FakeStoreProductRequestDTO fakeStoreProductRequestDTO){
        String postProductAPI = fakeStoreApiUrl+fakeStoreAPIPathProduct;
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductResponseDTO> response = restTemplate.postForEntity(postProductAPI,fakeStoreProductRequestDTO, FakeStoreProductResponseDTO.class);
        return response.getBody();
    }

    public boolean deleteProduct(int id){
        String deleteProductAPI = fakeStoreApiUrl+fakeStoreAPIPathProduct+"/"+id;
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.delete(deleteProductAPI);
        return true;
    }

    public boolean updateProduct(int id, FakeStoreProductRequestDTO fakeStoreProductRequestDTO){
        String updateProductUrl = fakeStoreApiUrl+fakeStoreAPIPathProduct+"/"+id;
        RestTemplate restTemplate = restTemplateBuilder.build();
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(id));
        restTemplate.put(updateProductUrl, fakeStoreProductRequestDTO, params);
        return true;
    }
}
