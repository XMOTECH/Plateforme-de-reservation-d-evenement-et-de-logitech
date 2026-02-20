package com.example.spring_app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_app.models.Event;
import com.example.spring_app.models.User;
import com.example.spring_app.repositories.EventRepository;
import com.example.spring_app.repositories.UserRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    

    @Autowired
    private UserRepository userRepository;


    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    public Event createEvent(Event event, Long userId) {

        User organizer = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Organisateur introuvable avec l'ID : " + userId));

        event.setOrganizer(organizer);
        
        return eventRepository.save(event);
    }

    
}
