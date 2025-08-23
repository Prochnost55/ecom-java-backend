package com.prochnost.ecom.backend.dto.orderDto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderListResponseDTO {
    private List<OrderResponseDTO> orderList;
    
    public OrderListResponseDTO() {
        orderList = new ArrayList<>();
    }
}
