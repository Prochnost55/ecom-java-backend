package com.prochnost.ecom.backend.service.priceService;

import com.prochnost.ecom.backend.dto.priceDto.PriceRequestDTO;
import com.prochnost.ecom.backend.dto.priceDto.PriceResponseDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductRequestDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.exceptions.PriceNotFoundException;
import com.prochnost.ecom.backend.mapper.PriceMapper;
import com.prochnost.ecom.backend.model.Price;
import com.prochnost.ecom.backend.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PriceServiceImpl implements PriceService{

    @Autowired
    PriceRepository priceRepository;
    @Override
    public PriceResponseDTO createPrice(PriceRequestDTO priceRequestDTO) {
        Price price = PriceMapper.priceRequestDtoToPrice(priceRequestDTO);
        price = priceRepository.save(price);
        return PriceMapper.priceToPriceResponseDto(price);
    }

    @Override
    public boolean updatePriceById(UUID id, PriceRequestDTO priceRequestDTO) throws PriceNotFoundException {
        Price price = priceRepository.findById(id).orElseThrow(() -> new PriceNotFoundException("Price not found with id:" + id));
        price.setCurrency(priceRequestDTO.getCurrency());
        price.setAmount(priceRequestDTO.getAmount());
        price.setDiscount(priceRequestDTO.getDiscount());
        priceRepository.save(price);
        return true;
    }
}
