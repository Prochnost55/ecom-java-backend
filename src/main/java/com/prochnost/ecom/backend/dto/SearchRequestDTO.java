package com.prochnost.ecom.backend.dto;

import com.prochnost.ecom.backend.model.Category;
import com.prochnost.ecom.backend.model.SortParams;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.prochnost.ecom.backend.model.Product}
 */
@Value
@Getter
@Setter
public class SearchRequestDTO implements Serializable {
    private String title;
    private int pageNumber;
    private int itemsPerPage;
    private List<SortParams> sortBy;

}