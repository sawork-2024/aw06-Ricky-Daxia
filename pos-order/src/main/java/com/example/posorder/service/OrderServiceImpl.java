package com.example.posorder.service;

import com.example.posorder.dto.OrderItemRequestDto;
import com.example.posorder.dto.ProductDto;
import com.example.posorder.model.OrderItemRequest;
import com.example.posorder.model.OrderRequest;
import com.example.posorder.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final RestTemplate restTemplate;

    private static final String PRODUCT_SERVICE_URL = "http://pos-product/api/products/";

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }
    
    @Transactional
    public boolean placeOrder(OrderRequest orderRequest) {
        List<OrderItemRequest> items = orderRequest.getItems();
        for (OrderItemRequest item: items) {
            HttpHeaders headers = new HttpHeaders();
            // headers.setContentType(MediaType.APPLICATION_JSON); 
            HttpEntity<ProductDto> requestEntity = new HttpEntity<>(null, headers);
            ResponseEntity<ProductDto> response = restTemplate.exchange(PRODUCT_SERVICE_URL + item.getProductId(), HttpMethod.GET, requestEntity, ProductDto.class);
            
            if (response.getStatusCode() != HttpStatus.OK) {
                return false;
            }
            ProductDto product = response.getBody();
            if (product == null || product.getQuantity() < item.getQuantity()) {
                return false;
            } else {
                item.setQuantity(product.getQuantity() - item.getQuantity()); // update target quantity
            }
        }
        // expected: every product has enough stock, try updating stock
        for (OrderItemRequest item: items) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<OrderItemRequestDto> requestEntity = new HttpEntity<>(new OrderItemRequestDto(item.getProductId(), item.getQuantity()), headers);
            ResponseEntity<Void> response = restTemplate.exchange(PRODUCT_SERVICE_URL + "update", HttpMethod.POST, requestEntity, Void.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                System.err.println("stock update failed for product " + item.getProductId() + " with quantity " + item.getQuantity());
                return false;
            }
        }
        return true;
    }
}
