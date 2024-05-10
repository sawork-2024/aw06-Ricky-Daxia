package com.example.posorder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {
    
    private String productId;

    private int quantity;
}
