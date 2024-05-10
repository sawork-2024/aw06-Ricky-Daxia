package com.example.posorder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String id;

    private String _id;

    private String price;

    private String category;

    private int quantity;

    private String name;

    private int stock;

    private String img;    
}
