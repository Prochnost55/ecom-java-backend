package com.prochnost.ecom.backend.service.productService;

import com.prochnost.ecom.backend.dto.productDto.ProductListResponseDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductRequestDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.exceptions.PriceNotFoundException;
import com.prochnost.ecom.backend.exceptions.ProductNotFoundException;
import com.prochnost.ecom.backend.mapper.ProductMapper;
import com.prochnost.ecom.backend.model.Category;
import com.prochnost.ecom.backend.model.Price;
import com.prochnost.ecom.backend.model.Product;
import com.prochnost.ecom.backend.repository.CategoryRepository;
import com.prochnost.ecom.backend.repository.PriceRepository;
import com.prochnost.ecom.backend.repository.ProductRepository;
import com.prochnost.ecom.backend.service.sync.ProductSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.prochnost.ecom.backend.utils.ProductUtils.isNull;

@Service("productService")
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PriceRepository priceRepository;
    
    @Autowired
    private ProductSyncService productSyncService;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              PriceRepository priceRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.priceRepository = priceRepository;
    }
    @Override
    @Cacheable(value = "products")
    public ProductListResponseDTO getAllProducts() {
        List<Product> products =  productRepository.findAll();
        ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();

        for(Product p: products){
            productListResponseDTO.getProductList().add(ProductMapper.productToProductResponseDTO(p));
        }
        return productListResponseDTO;
    }

    @Override
    @Cacheable(value = "product", key = "#id")
    public ProductResponseDTO getProductById(UUID id) throws ProductNotFoundException {
        Product p = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found with id : " + id));
        return ProductMapper.productToProductResponseDTO(p);
    }

    @Override
    public ProductResponseDTO getProductByTitle(String title) throws ProductNotFoundException {
        Product filteredProduct = productRepository.getProductByTitle(title);
        if(isNull(filteredProduct)){
            throw new ProductNotFoundException("Product not found with title : " + title);
        }
        return ProductMapper.productToProductResponseDTO(filteredProduct);
    }

    @Override
    public ProductListResponseDTO getProductsByCategory(String categoryName) {
        List<Product> products = productRepository.findByCategoryCategoryNameIgnoreCase(categoryName);
        ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();
        
        for(Product p: products){
            productListResponseDTO.getProductList().add(ProductMapper.productToProductResponseDTO(p));
        }
        return productListResponseDTO;
    }

    @Override
    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = ProductMapper.ProductRequestDtoToProduct(productRequestDTO);
        Category category = product.getCategory();
        category = categoryRepository.save(category);

        Price price = product.getPrice();
        price = priceRepository.save(price);

        product.setCategory(category);
        product.setPrice(price);

        Product savedProduct = productRepository.save(product);
        
        // Sync to Elasticsearch
        productSyncService.syncProductToElasticsearch(savedProduct);
        
        ProductResponseDTO productResponseDTO = ProductMapper.productToProductResponseDTO(savedProduct);
        return productResponseDTO;
    }

    @Override
    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public boolean deleteProduct(UUID id) {
        productRepository.deleteById(id);
        
        // Remove from Elasticsearch
        productSyncService.removeProductFromElasticsearch(id.toString());
        
        return true;
    }

    @Override
    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public boolean updateProduct(UUID id, ProductRequestDTO updatedProductDTO) throws ProductNotFoundException{
        Product productToUpdate = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product does not exist with id : " + id));
        Product updatedProduct = ProductMapper.ProductRequestDtoToProduct(updatedProductDTO);

        Price price = productToUpdate.getPrice();
        price.setDiscount(updatedProduct.getPrice().getDiscount());
        price.setAmount(updatedProduct.getPrice().getAmount());
        price.setCurrency(updatedProduct.getPrice().getCurrency());
        productToUpdate.setPrice(price);

        productToUpdate.setDescription(updatedProduct.getDescription());
        productToUpdate.setImage(updatedProduct.getImage());
        productToUpdate.setTitle(updatedProduct.getTitle());

        Category category = productToUpdate.getCategory();
        category.setCategoryName(updatedProduct.getCategory().getCategoryName());
        productToUpdate.setCategory(category);

        Product savedProduct = productRepository.save(productToUpdate);
        
        // Sync updated product to Elasticsearch
        productSyncService.syncProductToElasticsearch(savedProduct);
        
        return true;
    }
}
