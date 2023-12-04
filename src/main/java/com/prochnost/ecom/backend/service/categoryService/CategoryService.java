package com.prochnost.ecom.backend.service.categoryService;

import com.prochnost.ecom.backend.dto.categoryDto.CategoryListResponseDTO;
import com.prochnost.ecom.backend.dto.categoryDto.CategoryRequestDTO;
import com.prochnost.ecom.backend.dto.categoryDto.CategoryResponseDTO;
import com.prochnost.ecom.backend.exceptions.CategoryNotFoundException;
import com.prochnost.ecom.backend.model.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);
    public CategoryListResponseDTO getAllCategories();
    public CategoryResponseDTO getCategoryById(UUID categoryId) throws CategoryNotFoundException;
    public boolean updateCategoryById(UUID id, CategoryRequestDTO category) throws CategoryNotFoundException;
    public boolean deleteCategoryById(UUID id);
}
