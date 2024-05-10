package com.example.posproduct.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor // Without it, causes error
public class Product implements Serializable {

    @Id 
    @Column(name = "id")
    private String id;
    @Column(name = "_id")
    private String _id;
    @Column(name = "price")
    private String price;
    @Column(name = "category")
    private String category;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "name")
    private String name;
    @Column(name = "stock")
    private int stock;
    @Column(name = "img")
    private String img;
}
