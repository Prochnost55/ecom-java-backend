package com.prochnost.ecom.backend.controller;

import com.prochnost.ecom.backend.dto.categoryDto.CategoryListResponseDTO;
import com.prochnost.ecom.backend.dto.categoryDto.CategoryRequestDTO;
import com.prochnost.ecom.backend.dto.categoryDto.CategoryResponseDTO;
import com.prochnost.ecom.backend.exceptions.CategoryNotFoundException;
import com.prochnost.ecom.backend.service.categoryService.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<CategoryListResponseDTO> getAllCategories() {
        CategoryListResponseDTO response = categoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable String id) throws CategoryNotFoundException {
        CategoryResponseDTO response = categoryService.getCategoryById(UUID.fromString(id));
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO response = categoryService.createCategory(categoryRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateCategory(@PathVariable String id, @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) throws CategoryNotFoundException {
        boolean response = categoryService.updateCategoryById(UUID.fromString(id), categoryRequestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable String id) {
        boolean response = categoryService.deleteCategoryById(UUID.fromString(id));
        return ResponseEntity.ok(response);
    }
}
