package com.example.spring_app.services;

import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import lombok.Value;

@Service
public class PaymentService {
    
    private String stripeApiKey;
}
