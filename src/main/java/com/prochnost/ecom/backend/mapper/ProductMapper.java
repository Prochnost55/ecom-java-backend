package com.prochnost.ecom.backend.mapper;

import com.prochnost.ecom.backend.dto.productDto.FakeStoreProductRequestDTO;
import com.prochnost.ecom.backend.dto.productDto.FakeStoreProductResponseDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductRequestDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.model.Category;
import com.prochnost.ecom.backend.model.Price;
import com.prochnost.ecom.backend.model.Product;

public class ProductMapper {
    public static ProductResponseDTO fakeStoreProductResponseToProductResponse(FakeStoreProductResponseDTO fakeStoreProductResponseDTO) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setImage(fakeStoreProductResponseDTO.getImage());
//        productResponseDTO.setId(fakeStoreProductResponseDTO.getId());
        productResponseDTO.setDescription(fakeStoreProductResponseDTO.getDescription());
        productResponseDTO.setTitle(fakeStoreProductResponseDTO.getTitle());
        productResponseDTO.setPrice(fakeStoreProductResponseDTO.getPrice());
        productResponseDTO.setCategory(fakeStoreProductResponseDTO.getCategory());
        return  productResponseDTO;
    }

    public static FakeStoreProductRequestDTO productRequestToFakeStoreProductRequest(ProductRequestDTO productRequestDTO){
        FakeStoreProductRequestDTO fakeStoreProductRequestDTO = new FakeStoreProductRequestDTO();
        fakeStoreProductRequestDTO.setCategory(productRequestDTO.getCategory());
        fakeStoreProductRequestDTO.setDescription(productRequestDTO.getDescription());
        fakeStoreProductRequestDTO.setTitle(productRequestDTO.getTitle());
        fakeStoreProductRequestDTO.setPrice(productRequestDTO.getPrice());
        fakeStoreProductRequestDTO.setImage(productRequestDTO.getImage());
        return fakeStoreProductRequestDTO;
    }

    public static ProductResponseDTO productToProductResponseDTO(Product p){
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(p.getId());
        productResponseDTO.setCategory(p.getCategory().getCategoryName());
        productResponseDTO.setImage(p.getImage());
        productResponseDTO.setTitle(p.getTitle());
        productResponseDTO.setDescription(p.getDescription());
        productResponseDTO.setPrice(p.getPrice().getAmount());

        return productResponseDTO;
    }

    public static Product ProductRequestDtoToProduct(ProductRequestDTO productRequestDTO){
        Product product = new Product();

        Price price = new Price();
        price.setAmount(productRequestDTO.getPrice());
        product.setPrice(price);

        Category category = new Category();
        category.setCategoryName(productRequestDTO.getCategory());
        product.setCategory(category);

        product.setDescription(productRequestDTO.getDescription());

        product.setImage(productRequestDTO.getImage());

        product.setTitle(productRequestDTO.getTitle());

        return product;
    }
}