package com.example.spring_app.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ticket")
@Getter @Setter
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false)
    private String uniqueCode;

    private Boolean isScanned=false;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "ticket_category_id")
    private TicketCategory ticketCategory;
    
    @PrePersist
    protected void onCreate(){
        if (this.uniqueCode == null) {
            this.uniqueCode = UUID.randomUUID().toString();
        }
    }
}
