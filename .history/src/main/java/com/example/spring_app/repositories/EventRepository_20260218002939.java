package com.example.spring_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_app.models.Event;

import org.springframework.stereotype.Repository;

public interface EventRepository extends JpaRepository<Event, Long>{
    
}
