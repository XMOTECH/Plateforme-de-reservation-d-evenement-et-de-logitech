package com.example.spring_app.repositories;

import com.example.spring_app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
