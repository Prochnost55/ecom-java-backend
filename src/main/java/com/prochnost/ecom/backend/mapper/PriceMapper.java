package com.prochnost.ecom.backend.mapper;

import com.prochnost.ecom.backend.dto.categoryDto.CategoryRequestDTO;
import com.prochnost.ecom.backend.dto.categoryDto.CategoryResponseDTO;
import com.prochnost.ecom.backend.dto.priceDto.PriceRequestDTO;
import com.prochnost.ecom.backend.dto.priceDto.PriceResponseDTO;
import com.prochnost.ecom.backend.model.Category;
import com.prochnost.ecom.backend.model.Price;

public class PriceMapper {
    public static Price priceRequestDtoToPrice(PriceRequestDTO priceRequestDTO) {
        Price price = new Price();
        price.setAmount(priceRequestDTO.getAmount());
        price.setDiscount(priceRequestDTO.getDiscount());
        price.setCurrency(priceRequestDTO.getCurrency());
        return price;
    }
    public static PriceResponseDTO priceToPriceResponseDto(Price price) {
        PriceResponseDTO priceResponseDTO = new PriceResponseDTO();
        priceResponseDTO.setId(price.getId());
        priceResponseDTO.setAmount(price.getAmount());
        priceResponseDTO.setDiscount(price.getDiscount());
        priceResponseDTO.setCurrency(price.getCurrency());
        return priceResponseDTO;
    }

}
