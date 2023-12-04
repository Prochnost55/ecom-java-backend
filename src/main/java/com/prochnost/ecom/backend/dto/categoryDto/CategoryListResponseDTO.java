package com.prochnost.ecom.backend.dto.categoryDto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class CategoryListResponseDTO {
    private ArrayList<CategoryResponseDTO> categoryList;
    public CategoryListResponseDTO() {
        categoryList = new ArrayList<>();
    }
}
