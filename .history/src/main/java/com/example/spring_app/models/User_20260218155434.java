package com.example.spring_app.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter @Setter
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    @Column(unique = true, nullable = false)
    private String email;
}
