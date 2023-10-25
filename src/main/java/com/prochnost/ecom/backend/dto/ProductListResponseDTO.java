package com.prochnost.ecom.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ProductListResponseDTO {
    private ArrayList<ProductResponseDTO> productList;
    public ProductListResponseDTO() {
        productList = new ArrayList<>();
    }
}
