package com.prochnost.ecom.backend.mapper;

import com.prochnost.ecom.backend.dto.categoryDto.CategoryRequestDTO;
import com.prochnost.ecom.backend.dto.categoryDto.CategoryResponseDTO;
import com.prochnost.ecom.backend.model.Category;

public class CategoryMapper {

    public static Category categoryRequestDtoToCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category();
        category.setCategoryName(categoryRequestDTO.getCategoryName());
        return category;
    }

    public static CategoryResponseDTO categoryToCategoryResponseDto(Category category) {
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setCategoryName(category.getCategoryName());
        categoryResponseDTO.setId(category.getId());
        return categoryResponseDTO;
    }
}
