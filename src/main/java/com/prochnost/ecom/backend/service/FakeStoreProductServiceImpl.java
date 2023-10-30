package com.prochnost.ecom.backend.service;

import com.prochnost.ecom.backend.client.FakeStoreAPIClient;
import com.prochnost.ecom.backend.dto.*;
import com.prochnost.ecom.backend.exceptions.ProductNotFoundException;
import com.prochnost.ecom.backend.model.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.prochnost.ecom.backend.mapper.ProductMapper.fakeStoreProductResponseToProductResponse;
import static com.prochnost.ecom.backend.mapper.ProductMapper.productRequestToFakeStoreProductRequest;
import static com.prochnost.ecom.backend.utils.ProductUtils.isNull;

@Service("fakeStoreProductService")
public class FakeStoreProductServiceImpl implements ProductService{
    private RestTemplateBuilder restTemplateBuilder;
    private FakeStoreAPIClient fakeStoreAPIClient;
    public FakeStoreProductServiceImpl(RestTemplateBuilder restTemplateBuilder, FakeStoreAPIClient fakeStoreAPIClient){
        this.restTemplateBuilder = restTemplateBuilder;
        this.fakeStoreAPIClient = fakeStoreAPIClient;
    }
    @Override
    public ProductListResponseDTO getAllProducts() {
        ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();
        List<FakeStoreProductResponseDTO> fakeStoreProductResponseDTOs = fakeStoreAPIClient.getAllProducts();
        for(FakeStoreProductResponseDTO res : fakeStoreProductResponseDTOs){
            productListResponseDTO.getProductList().add(fakeStoreProductResponseToProductResponse(res));
        }
        return productListResponseDTO;
    }

    @Override
    public ProductResponseDTO getProductById(int id) throws ProductNotFoundException{
        FakeStoreProductResponseDTO fakeStoreResponse = fakeStoreAPIClient.getProduct(id);
        if(isNull(fakeStoreResponse)){
            throw new ProductNotFoundException("Product not found with id : " + id);
        }
        return fakeStoreProductResponseToProductResponse(fakeStoreResponse);
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        FakeStoreProductRequestDTO fakeStoreProductRequestDTO = productRequestToFakeStoreProductRequest(productRequestDTO);
        FakeStoreProductResponseDTO fakeStoreProductResponseDTO = fakeStoreAPIClient.createProduct(fakeStoreProductRequestDTO);
        return fakeStoreProductResponseToProductResponse(fakeStoreProductResponseDTO);
    }

    @Override
    public boolean deleteProduct(int id) {
        return fakeStoreAPIClient.deleteProduct(id);
    }

    @Override
    public Product updateProduct(int id, Product updatedProduct) {
        return null;
    }
}
