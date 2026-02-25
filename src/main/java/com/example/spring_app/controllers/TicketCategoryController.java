package com.example.spring_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_app.models.Event;
import com.example.spring_app.models.TicketCategory;
import com.example.spring_app.repositories.EventRepository;
import com.example.spring_app.repositories.TicketCategoryRepository;

@RestController
@RequestMapping("/api/ticket-categories")
public class TicketCategoryController {

    @Autowired
    private TicketCategoryRepository ticketCategoryRepository;

    @Autowired
    private EventRepository eventRepository;

    @PostMapping("/{eventId}")
    public ResponseEntity<TicketCategory> createCategory(@PathVariable Long eventId, @RequestBody TicketCategory category) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evénement non trouvé"));
        
        category.setEvent(event);
        return ResponseEntity.ok(ticketCategoryRepository.save(category));
    }
}
