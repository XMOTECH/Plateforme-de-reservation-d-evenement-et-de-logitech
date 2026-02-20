package com.example.spring_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.spring_app.models.TicketCategory;

public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {
    List<TicketCategory> findByEventId(Long eventId);
}
