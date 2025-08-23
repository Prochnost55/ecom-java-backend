package com.prochnost.ecom.backend.controller;

import com.prochnost.ecom.backend.dto.SearchRequestDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.service.searchService.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/search")
@Tag(name = "Product Search", description = "Advanced product search functionality")
public class SearchController {
    SearchService searchService;
    public SearchController(SearchService searchService){
        this.searchService = searchService;
    }
    
    @Operation(summary = "Search products", description = "Search products with advanced filtering and pagination")
    @PostMapping
    public Page<ProductResponseDTO> searchProduct(@Valid @RequestBody SearchRequestDTO searchRequestDTO){
        List<ProductResponseDTO> productResponseDTO = searchService.searchProduct(searchRequestDTO);
        // Create proper pagination with metadata
        Page<ProductResponseDTO> productResponseDTOPage = new PageImpl<>(
                productResponseDTO,
                org.springframework.data.domain.PageRequest.of(
                    searchRequestDTO.getPageNumber(), 
                    searchRequestDTO.getItemsPerPage()
                ),
                productResponseDTO.size() // This should ideally be total count from database
        );
        return productResponseDTOPage;
    }
}
