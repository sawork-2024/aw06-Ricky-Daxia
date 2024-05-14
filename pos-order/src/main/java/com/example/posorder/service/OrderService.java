package com.example.posorder.service;

import com.example.posorder.model.OrderRequest;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    
    ResponseEntity<?> placeOrder(OrderRequest orderRequest) throws Exception;
}
