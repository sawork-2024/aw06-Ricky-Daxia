package com.example.posorder.service;

import com.example.posorder.model.OrderRequest;

public interface OrderService {
    
    boolean placeOrder(OrderRequest orderRequest);
}
