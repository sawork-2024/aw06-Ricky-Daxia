package com.example.posproduct.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {

    private String id;

    private String _id;

    private String price;

    private String category;

    private int quantity;

    private String name;

    private int stock;

    private String img;
}
