package com.prochnost.ecom.backend.service.searchService;

import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.mapper.ProductMapper;
import com.prochnost.ecom.backend.model.Product;
import com.prochnost.ecom.backend.model.SortParams;
import com.prochnost.ecom.backend.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class SearchService {
    private ProductRepository productRepository;

    public SearchService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    public List<ProductResponseDTO> searchProduct(String query, int pageNumber, int pageSize, List<SortParams> sortBy){
        Sort sort = null;
        if(!isNull(sortBy) && !sortBy.isEmpty()) {
            if(sortBy.get(0).getOrder().equals("ASC")){
                sort = Sort.by(sortBy.get(0).getParamName()).ascending();
            } else {
                sort = Sort.by(sortBy.get(0).getParamName()).descending();
            }
            for(int i = 1; i < sortBy.size(); i++){
                if(sortBy.get(i).getOrder().equals("ASC")){
                    sort = sort.and(Sort.by(sortBy.get(i).getParamName()).ascending());
                } else {
                    sort = sort.and(Sort.by(sortBy.get(i).getParamName()).descending());
                }
            }
        }

        PageRequest pageRequest = null;
        if(isNull(sort)){
            pageRequest = PageRequest.of(pageNumber, pageSize);
        } else {
            pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        }
        List<Product> products = productRepository.findAllByTitleContainingIgnoreCase(query, pageRequest);
        List<ProductResponseDTO> productResponseDTO = new ArrayList<>();
        for(Product p: products){
            productResponseDTO.add(ProductMapper.productToProductResponseDTO(p));
        }
        return productResponseDTO;
    }
}
