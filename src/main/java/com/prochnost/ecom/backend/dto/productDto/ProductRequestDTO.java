package com.prochnost.ecom.backend.dto.productDto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class ProductRequestDTO {
    @NotBlank(message = "Product title cannot be blank")
    @Size(min = 2, max = 100, message = "Product title must be between 2 and 100 characters")
    private String title;
    
    @Positive(message = "Price must be positive")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    private double price;
    
    @NotBlank(message = "Category cannot be blank")
    private String category;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    @Pattern(regexp = "^(https?://).*", message = "Image must be a valid URL")
    private String image;
}
