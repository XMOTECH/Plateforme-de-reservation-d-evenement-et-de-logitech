package com.example.spring_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_app.dto.OrderRequest;
import com.example.spring_app.models.Order;
import com.example.spring_app.services.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestParam Long userId,
            @RequestBody OrderRequest request){

        try {
            Order order = orderService.createOrder(userId, request);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            // TODO: handle exception
            return ResponseEntity.badRequest().body(e.getMessage());
        }        
            }
}
