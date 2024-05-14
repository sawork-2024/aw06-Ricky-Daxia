package com.example.posproduct.controller;

import com.example.posproduct.dto.ItemDto;
import com.example.posproduct.dto.ProductDto;
import com.example.posproduct.model.Categories;
import com.example.posproduct.model.Product;
import com.example.posproduct.model.Setting;
import com.example.posproduct.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

//@CrossOrigin("*")
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

    private AtomicInteger cnt = new AtomicInteger(0);

    private void workload() {
        int[] a = new int[100010];
        int n = 100000;
        for (int i = 0; i < n; i++) {
            a[i] = n - i;
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (a[j] > a[j + 1]) {
                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
        }
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getProducts() {
        int x = cnt.getAndIncrement();
        if (x > 50 && x % 2 == 1) {
            System.out.println("heavy");
            Thread thread = new Thread(this::workload);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException ignored) {

            }
        }
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

    @GetMapping("/settings")
    public ResponseEntity<List<Setting>> getSettings() {
        List<Setting> settings = new ArrayList<>();
        settings.add(new Setting(1, "Standalone Point of Sale", "Store-Pos", "10086", "10087", "15968774896", "", "$", "10", "", "", "d36d"));
        return new ResponseEntity<List<Setting>>(settings, HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Categories>> getCategories() {
        List<Categories> categories = new ArrayList<>();
        categories.add(new Categories("1711853606", "drink", 1711853606));
        return new ResponseEntity<List<Categories>>(categories, HttpStatus.OK);
    }
}
