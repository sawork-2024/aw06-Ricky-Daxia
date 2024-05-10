package com.example.posorder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    
    private Long id;

    private OrderDto orderDto;

    private String productId;

    private int quantity;
}
