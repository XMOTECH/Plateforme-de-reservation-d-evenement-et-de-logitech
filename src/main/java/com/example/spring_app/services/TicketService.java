package com.example.spring_app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_app.models.Order;
import com.example.spring_app.models.Ticket;
import com.example.spring_app.repositories.TicketRepository;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    // public List<Ticket> generateTicketsForOrder(Order order, int quantity) {
    //     List<Ticket> generatedTickets  = new ArrayList<>();

        
    // }
}
