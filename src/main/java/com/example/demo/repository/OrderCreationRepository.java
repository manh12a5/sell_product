package com.example.demo.repository;

import com.example.demo.model.OrderCreation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCreationRepository extends JpaRepository<OrderCreation, Long> {
}
