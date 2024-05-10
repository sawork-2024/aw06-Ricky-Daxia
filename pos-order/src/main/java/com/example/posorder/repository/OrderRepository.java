package com.example.posorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.posorder.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    
}
