package com.prochnost.ecom.backend.controller;

import com.prochnost.ecom.backend.dto.SearchRequestDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.service.searchService.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    SearchService searchService;
    public SearchController(SearchService searchService){
        this.searchService = searchService;
    }
    @PostMapping
    public Page<ProductResponseDTO> searchProduct(@RequestBody SearchRequestDTO searchRequestDTO){
        List<ProductResponseDTO> productResponseDTO = searchService.searchProduct(
                searchRequestDTO.getTitle(),
                searchRequestDTO.getPageNumber(),
                searchRequestDTO.getItemsPerPage(),
                searchRequestDTO.getSortBy()
        );
        Page<ProductResponseDTO> productResponseDTOPage = new PageImpl<>(
                productResponseDTO
        );
        return productResponseDTOPage;
    }
}
