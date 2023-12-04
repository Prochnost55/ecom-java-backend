package com.prochnost.ecom.backend.dto.categoryDto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryResponseDTO {
    private UUID id;
    private String categoryName;
}
