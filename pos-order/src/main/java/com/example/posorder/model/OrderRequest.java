package com.example.posorder.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    
    private List<OrderItemRequest> items;
}
