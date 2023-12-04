package com.prochnost.ecom.backend.service;

import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.exceptions.ProductNotFoundException;
import com.prochnost.ecom.backend.model.Category;
import com.prochnost.ecom.backend.model.Price;
import com.prochnost.ecom.backend.model.Product;
import com.prochnost.ecom.backend.repository.ProductRepository;
import com.prochnost.ecom.backend.service.productService.ProductService;
import com.prochnost.ecom.backend.service.productService.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class ProductServiceImplTest {
    @Mock // this annotation mocks the repo. So that we can write unit cases which are not dependent
    ProductRepository productRepository;
    @InjectMocks // this annotation specifies where the mock would be injected.
    ProductServiceImpl productService;

    private String mockProductTitle = "Mock Product";
    private Product getMockProduct(UUID id){
        Category mockCategory = new Category();
        mockCategory.setCategoryName("Mock Category");

        Price mockPrice = new Price();
        mockPrice.setDiscount(10);
        mockPrice.setCurrency("INR");
        mockPrice.setAmount(234123);

        Product mockProduct = new Product();
        mockProduct.setTitle(mockProductTitle);
        mockProduct.setId(id);
        mockProduct.setCategory(mockCategory);
        mockProduct.setPrice(mockPrice);
        mockProduct.setImage("mock image");
        mockProduct.setDescription("mock description");

        return mockProduct;
    }
    @Test
    public void testGetProductByTitleSuccess() throws ProductNotFoundException {
        // Arrange - Setup all the relevant mock objects
        Product mockProduct = getMockProduct(UUID.randomUUID());

        // this line sets up the product repo mock
        Mockito.when(productRepository.getProductByTitle(mockProductTitle)).thenReturn(mockProduct);

        // Act
        ProductResponseDTO actualResponse = productService.getProductByTitle(mockProductTitle);

        // Assert
        Assertions.assertEquals(actualResponse.getTitle(), mockProduct.getTitle());
        Assertions.assertEquals(actualResponse.getCategory(), mockProduct.getCategory().getCategoryName());
        Assertions.assertEquals(actualResponse.getImage(), mockProduct.getImage());
        Assertions.assertEquals(actualResponse.getDescription(), mockProduct.getDescription());
        Assertions.assertEquals(actualResponse.getId(), mockProduct.getId());
        Assertions.assertEquals(actualResponse.getPrice(), mockProduct.getPrice().getAmount());

    }

    @Test
    public void testGetProductByTitleFailure() throws ProductNotFoundException {
        String mockProductTitle = "mock title";
        Mockito.when(productRepository.getProductByTitle(mockProductTitle)).thenReturn(null);
        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProductByTitle(mockProductTitle));
    }

    @Test
    public void testProductDelete() {
        UUID id = UUID.randomUUID();
        productService.deleteProduct(id);
        Mockito.verify(productRepository).deleteById(id);
    }

    @Test
    public void testGetProductByIdSuccess() throws ProductNotFoundException{
        UUID id = UUID.randomUUID();
        Product mockProduct = getMockProduct(id);

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(mockProduct));
        ProductResponseDTO actualResponse = productService.getProductById(id);

        Assertions.assertEquals(actualResponse.getTitle(), mockProduct.getTitle());
        Assertions.assertEquals(actualResponse.getCategory(), mockProduct.getCategory().getCategoryName());
        Assertions.assertEquals(actualResponse.getImage(), mockProduct.getImage());
        Assertions.assertEquals(actualResponse.getDescription(), mockProduct.getDescription());
        Assertions.assertEquals(actualResponse.getId(), mockProduct.getId());
        Assertions.assertEquals(actualResponse.getPrice(), mockProduct.getPrice().getAmount());
    }

    @Test
    public void testGetProductByIdFailure() throws ProductNotFoundException{
        UUID id = UUID.randomUUID();
        Mockito.when(productRepository.findById(id)).thenReturn(null);
        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProductById(id));
    }
}
