package com.example.spring_app.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter; 

@Entity
@Getter @Setter
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le titre est requis")
    @Size(min = 3, max = 100, message = "Le titre doit faire entre 3 et 100 caractères")
    private String title;
    private String description;
    private LocalDateTime eventDateTime;

    @PositiveOrZero(message = "Le prix ne peut pas être négatif")
    private Double price;
    
    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;
}
