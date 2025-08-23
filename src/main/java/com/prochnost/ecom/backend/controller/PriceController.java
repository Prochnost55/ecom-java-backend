package com.prochnost.ecom.backend.controller;

import com.prochnost.ecom.backend.dto.priceDto.PriceRequestDTO;
import com.prochnost.ecom.backend.dto.priceDto.PriceResponseDTO;
import com.prochnost.ecom.backend.exceptions.PriceNotFoundException;
import com.prochnost.ecom.backend.service.priceService.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/prices")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @PostMapping("")
    public ResponseEntity<PriceResponseDTO> createPrice(@RequestBody PriceRequestDTO priceRequestDTO) {
        PriceResponseDTO response = priceService.createPrice(priceRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updatePrice(@PathVariable String id, @RequestBody PriceRequestDTO priceRequestDTO) throws PriceNotFoundException {
        boolean response = priceService.updatePriceById(UUID.fromString(id), priceRequestDTO);
        return ResponseEntity.ok(response);
    }
}
