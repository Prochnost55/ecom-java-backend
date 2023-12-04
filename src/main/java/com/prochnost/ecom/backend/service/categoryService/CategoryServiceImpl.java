package com.prochnost.ecom.backend.service.categoryService;

import com.prochnost.ecom.backend.dto.categoryDto.CategoryListResponseDTO;
import com.prochnost.ecom.backend.dto.categoryDto.CategoryRequestDTO;
import com.prochnost.ecom.backend.dto.categoryDto.CategoryResponseDTO;
import com.prochnost.ecom.backend.exceptions.CategoryNotFoundException;
import com.prochnost.ecom.backend.mapper.CategoryMapper;
import com.prochnost.ecom.backend.model.Category;
import com.prochnost.ecom.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = CategoryMapper.categoryRequestDtoToCategory(categoryRequestDTO);
        category = categoryRepository.save(category);
        return CategoryMapper.categoryToCategoryResponseDto(category);
    }

    @Override
    public CategoryListResponseDTO getAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        CategoryListResponseDTO categoryListResponseDTO = new CategoryListResponseDTO();
        for(Category category: categoryList){
            categoryListResponseDTO.getCategoryList().add(CategoryMapper.categoryToCategoryResponseDto(category));
        }
        return categoryListResponseDTO;
    }

    @Override
    public CategoryResponseDTO getCategoryById(UUID categoryId) throws CategoryNotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Category not found for ID: " + categoryId));
        return CategoryMapper.categoryToCategoryResponseDto(category);
    }

    @Override
    public boolean updateCategoryById(UUID id, CategoryRequestDTO categoryRequestDTO) throws CategoryNotFoundException{
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found for ID: " + id));
        category.setCategoryName(categoryRequestDTO.getCategoryName());
        categoryRepository.save(category);
        return true;
    }

    @Override
    public boolean deleteCategoryById(UUID id) {
        categoryRepository.deleteById(id);
        return true;
    }
}
