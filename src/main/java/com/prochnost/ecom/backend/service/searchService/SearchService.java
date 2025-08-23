package com.prochnost.ecom.backend.service.searchService;

import com.prochnost.ecom.backend.dto.SearchRequestDTO;
import com.prochnost.ecom.backend.dto.productDto.ProductResponseDTO;
import com.prochnost.ecom.backend.mapper.ProductMapper;
import com.prochnost.ecom.backend.model.Product;
import com.prochnost.ecom.backend.model.SortParams;
import com.prochnost.ecom.backend.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class SearchService {
    private ProductRepository productRepository;

    public SearchService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    
    public List<ProductResponseDTO> searchProduct(SearchRequestDTO searchRequest){
        Sort sort = buildSort(searchRequest.getSortBy());
        PageRequest pageRequest = isNull(sort) ? 
            PageRequest.of(searchRequest.getPageNumber(), searchRequest.getItemsPerPage()) :
            PageRequest.of(searchRequest.getPageNumber(), searchRequest.getItemsPerPage(), sort);
            
        Specification<Product> spec = buildSpecification(searchRequest);
        List<Product> products = productRepository.findAll(spec, pageRequest).getContent();
        
        List<ProductResponseDTO> productResponseDTO = new ArrayList<>();
        for(Product p: products){
            productResponseDTO.add(ProductMapper.productToProductResponseDTO(p));
        }
        return productResponseDTO;
    }
    
    private Sort buildSort(List<SortParams> sortBy) {
        if(isNull(sortBy) || sortBy.isEmpty()) {
            return null;
        }
        
        Sort sort = null;
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
        return sort;
    }
    
    private Specification<Product> buildSpecification(SearchRequestDTO searchRequest) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (searchRequest.getTitle() != null && !searchRequest.getTitle().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")), 
                    "%" + searchRequest.getTitle().toLowerCase() + "%"
                ));
            }
            
            if (searchRequest.getCategory() != null && !searchRequest.getCategory().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("category").get("categoryName")), 
                    "%" + searchRequest.getCategory().toLowerCase() + "%"
                ));
            }
            
            if (searchRequest.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("price").get("amount"), 
                    searchRequest.getMinPrice()
                ));
            }
            
            if (searchRequest.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("price").get("amount"), 
                    searchRequest.getMaxPrice()
                ));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
