package com.prochnost.ecom.backend.dto.priceDto;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.prochnost.ecom.backend.model.Price}
 */

@Getter
@Setter
public class PriceRequestDTO {
    String currency;
    double amount;
    double discount;
}