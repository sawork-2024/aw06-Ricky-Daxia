package com.example.posproduct.service;

import java.util.List;

import com.example.posproduct.model.Product;

public interface ProductService {

    public List<Product> getProducts();

    public Product findByID(String productid);

    public boolean updateProductQuantityById(String productId, int quantity);
}
