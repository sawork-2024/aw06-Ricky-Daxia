package com.example.posorder.controller;

import com.example.posorder.dto.OrderRequestDto;
import com.example.posorder.model.OrderRequest;
import com.example.posorder.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin("*")
@RequestMapping("api")
@RestController
public class OrderController {
    
    private final ModelMapper orderMapper;
    
    private final OrderService orderService;

    @Autowired
    public OrderController(ModelMapper modelMapper, OrderService orderService) {
        this.orderMapper = modelMapper;
        this.orderService = orderService;
    }

    @PostMapping(path = "/orders")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequestDto orderRequestDto) {
        System.out.println("in orders");
        OrderRequest orderRequest = orderMapper.map(orderRequestDto, OrderRequest.class);
        boolean status = orderService.placeOrder(orderRequest);
        if (status) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
}
