package com.prochnost.ecom.backend.service.initService;

import com.prochnost.ecom.backend.model.Category;
import com.prochnost.ecom.backend.model.Order;
import com.prochnost.ecom.backend.model.Price;
import com.prochnost.ecom.backend.model.Product;
import com.prochnost.ecom.backend.repository.CategoryRepository;
import com.prochnost.ecom.backend.repository.OrderRepository;
import com.prochnost.ecom.backend.repository.PriceRepository;
import com.prochnost.ecom.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InitServiceImpl implements InitService{
    private CategoryRepository categoryRepository;
    private OrderRepository orderRepository;
    private PriceRepository priceRepository;
    private ProductRepository productRepository;

    public InitServiceImpl(CategoryRepository categoryRepository, OrderRepository orderRepository, PriceRepository priceRepository, ProductRepository productRepository){
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.priceRepository = priceRepository;
        this.productRepository = productRepository;
    }
    @Override
    public void initialise() {
        Category Electronics = new Category();
        Electronics.setCategoryName("electronics");

        Electronics = categoryRepository.save(Electronics);

        Price IphonePrice = new Price();
        IphonePrice.setCurrency("INR");
        IphonePrice.setAmount(100000.00);
        IphonePrice.setDiscount(0);

        Price IpadPrice = new Price();
        IpadPrice.setCurrency("INR");
        IpadPrice.setAmount(50000.00);
        IpadPrice.setDiscount(0);

        Price MacPrice = new Price();
        MacPrice.setCurrency("INR");
        MacPrice.setAmount(150000.00);
        MacPrice.setDiscount(0);

        IpadPrice = priceRepository.save(IpadPrice);
        IphonePrice = priceRepository.save(IphonePrice);
        MacPrice = priceRepository.save(MacPrice);

        Product iPhone = new Product();
        iPhone.setCategory(Electronics);
        iPhone.setPrice(IphonePrice);
        iPhone.setTitle("IPhone 15 Pro");
        iPhone.setDescription("Best Iphone ever");
        iPhone.setImage("https://picsum.photos/300");
        iPhone = productRepository.save(iPhone);


        Product iPad = new Product();
        iPad.setCategory(Electronics);
        iPad.setPrice(IpadPrice);
        iPad.setTitle("iPad Air 5");
        iPad.setDescription("Best Ipad ever");
        iPad.setImage("https://picsum.photos/300");
        iPad = productRepository.save(iPad);


        Product macbook = new Product();
        macbook.setCategory(Electronics);
        macbook.setPrice(MacPrice);
        macbook.setTitle("Macbook Pro 16");
        macbook.setDescription("Best macbook ever");
        macbook.setImage("https://picsum.photos/300");
        macbook = productRepository.save(macbook);

        Order order = new Order();
        order.setProducts(List.of(macbook, iPhone, iPad));
        order = orderRepository.save(order);
    }
}
