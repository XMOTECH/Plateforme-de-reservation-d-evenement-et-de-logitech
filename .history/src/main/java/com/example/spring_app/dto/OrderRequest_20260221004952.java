package com.example.spring_app.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private Long eventId;
    private Long categoryId;
    private Integer quantity;
}
