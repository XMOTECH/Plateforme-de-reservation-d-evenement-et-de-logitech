package com.example.spring_app.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_app.dto.OrderRequest;
import com.example.spring_app.models.Event;
import com.example.spring_app.models.Order;
import com.example.spring_app.models.OrderStatus;
import com.example.spring_app.models.TicketCategory;
import com.example.spring_app.models.User;
import com.example.spring_app.repositories.EventRepository;
import com.example.spring_app.repositories.OrderRepository;
import com.example.spring_app.repositories.TicketCategoryRepository;
import com.example.spring_app.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketCategoryRepository ticketCategoryRepository;

    @Transactional
    public Order createOrder(Long userId, OrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Evenement introuvable")); 
                
        TicketCategory ticketCategory = ticketCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categorie de Billet introuvable"));                  
                
        int ticketsDejaVendus = event.getTicketsSold() != null ? event.getTicketsSold() : 0;
        int placesRestantes = event.getCapacity() - ticketsDejaVendus;

        if (request.getQuantity() > placesRestantes) {
            throw new RuntimeException("Plus assez de places");
        }

        event.setTicketsSold(ticketsDejaVendus + request.getQuantity());
        eventRepository.save(event);

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        BigDecimal quantityBD = BigDecimal.valueOf(request.getQuantity());
        BigDecimal totalAmount = ticketCategory.getPrice().multiply(quantityBD);
        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }
}
