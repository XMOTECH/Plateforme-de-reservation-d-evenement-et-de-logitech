package com.example.spring_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_app.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
