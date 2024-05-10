package com.example.posproduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.posproduct.model.Product;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.quantity = :quantity WHERE p.id = :productId") // Product should be entity not table name
    int updateProductQuantityById(@Param("productId") String productId, @Param("quantity") int quantity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Product findById(String productId);
}
