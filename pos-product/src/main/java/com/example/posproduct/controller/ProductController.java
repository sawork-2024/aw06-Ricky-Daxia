package com.example.posproduct.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.posproduct.dto.ItemDto;
import com.example.posproduct.dto.ProductDto;
import com.example.posproduct.model.Product;
import com.example.posproduct.service.ProductService;

@CrossOrigin("*")
@RequestMapping("api")
@RestController
public class ProductController {

    private final ModelMapper productMapper;
    
    private final ProductService productService;

    @Autowired // 推荐的注入方式
    public ProductController(ModelMapper productMapper, ProductService productService) {
        this.productMapper = productMapper;
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> products = productService.getProducts().stream()
            .map(product -> productMapper.map(product, ProductDto.class)).collect(Collectors.toList());
        if (products == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String productId) {
        System.out.println(productId);
        Product product = productService.findByID(productId);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ProductDto productDto = productMapper.map(product, ProductDto.class);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @PostMapping("/products/update")
    public ResponseEntity<Void> updateProductQuantityById(@RequestBody ItemDto itemDto) {
        String productId = itemDto.getProductId();
        int quantity = itemDto.getQuantity();
        if (productService.updateProductQuantityById(productId, quantity)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
