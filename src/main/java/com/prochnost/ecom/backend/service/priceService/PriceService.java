package com.prochnost.ecom.backend.service.priceService;

import com.prochnost.ecom.backend.dto.categoryDto.CategoryListResponseDTO;
import com.prochnost.ecom.backend.dto.categoryDto.CategoryRequestDTO;
import com.prochnost.ecom.backend.dto.categoryDto.CategoryResponseDTO;
import com.prochnost.ecom.backend.dto.priceDto.PriceRequestDTO;
import com.prochnost.ecom.backend.dto.priceDto.PriceResponseDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductRequestDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.exceptions.CategoryNotFoundException;
import com.prochnost.ecom.backend.exceptions.PriceNotFoundException;

import java.util.UUID;

public interface PriceService {
    public PriceResponseDTO createPrice(PriceRequestDTO priceRequestDTO);
//    public CategoryListResponseDTO getAllPrices();
//    public CategoryResponseDTO getPriceById(UUID categoryId) throws CategoryNotFoundException;
    public boolean updatePriceById(UUID id, PriceRequestDTO category) throws PriceNotFoundException;
//    public boolean deletePriceById(UUID id);
}
