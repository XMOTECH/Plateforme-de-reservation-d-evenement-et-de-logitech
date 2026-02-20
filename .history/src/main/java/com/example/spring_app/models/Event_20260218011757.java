package com.example.spring_app.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter; 

@Entity
@Getter @Setter
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private LocalDateTime eventDateTime;
    private Double price;
    
    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    // À ajouter à l'intérieur de ta classe Event.java
    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public User getOrganizer() {
        return organizer;
    }

}
