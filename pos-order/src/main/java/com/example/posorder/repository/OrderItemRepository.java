package com.example.posorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.posorder.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    
}
