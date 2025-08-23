package com.prochnost.ecom.backend.service.productService;

import com.prochnost.ecom.backend.client.FakeStoreAPIClient;
import com.prochnost.ecom.backend.dto.productDto.*;
import com.prochnost.ecom.backend.exceptions.ProductNotFoundException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
    public ProductResponseDTO getProductById(UUID id) throws ProductNotFoundException{
        FakeStoreProductResponseDTO fakeStoreResponse = fakeStoreAPIClient.getProduct(1);
        if(isNull(fakeStoreResponse)){
            throw new ProductNotFoundException("Product not found with id : " + id);
        }
        return fakeStoreProductResponseToProductResponse(fakeStoreResponse);
    }

    @Override
    public ProductResponseDTO getProductByTitle(String title) throws ProductNotFoundException {
        // FakeStore API doesn't support search by title, so we get all products and filter
        List<FakeStoreProductResponseDTO> allProducts = fakeStoreAPIClient.getAllProducts();
        
        for (FakeStoreProductResponseDTO product : allProducts) {
            if (product.getTitle().equalsIgnoreCase(title)) {
                return fakeStoreProductResponseToProductResponse(product);
            }
        }
        
        throw new ProductNotFoundException("Product not found with title: " + title);
    }

    @Override
    public ProductListResponseDTO getProductsByCategory(String categoryName) {
        // FakeStore API doesn't have category filtering, so get all and filter
        List<FakeStoreProductResponseDTO> allProducts = fakeStoreAPIClient.getAllProducts();
        ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();
        
        for (FakeStoreProductResponseDTO product : allProducts) {
            if (product.getCategory().equalsIgnoreCase(categoryName)) {
                productListResponseDTO.getProductList().add(fakeStoreProductResponseToProductResponse(product));
            }
        }
        return productListResponseDTO;
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        FakeStoreProductRequestDTO fakeStoreProductRequestDTO = productRequestToFakeStoreProductRequest(productRequestDTO);
        FakeStoreProductResponseDTO fakeStoreProductResponseDTO = fakeStoreAPIClient.createProduct(fakeStoreProductRequestDTO);
        return fakeStoreProductResponseToProductResponse(fakeStoreProductResponseDTO);
    }

    @Override
    public boolean deleteProduct(UUID id) {
        return fakeStoreAPIClient.deleteProduct(1);
    }

    @Override
    public boolean updateProduct(UUID id, ProductRequestDTO updatedProduct) throws ProductNotFoundException {
        FakeStoreProductRequestDTO fakeStoreProductRequestDTO = productRequestToFakeStoreProductRequest(updatedProduct);
        return fakeStoreAPIClient.updateProduct(1, fakeStoreProductRequestDTO);
    }
}
