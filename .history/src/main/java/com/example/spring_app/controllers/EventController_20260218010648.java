package com.example.spring_app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_app.models.Event;
import com.example.spring_app.services.EventService;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public List<Event> getAll() {
        return eventService.getAllEvents();
    }
    
    @PostMapping("/{userId}")
    public Event create(@RequestBody Event event, @PathVariable Long userId) {
        return eventService.createEvent(event, userId);
    }
}
