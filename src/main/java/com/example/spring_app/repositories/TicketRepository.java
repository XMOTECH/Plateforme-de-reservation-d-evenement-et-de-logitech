package com.example.spring_app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_app.models.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByOrderId(Long orderId);

    Optional<Ticket> findByUniqueCode(String uniqueCode);
}
