package com.example.wigellkoncernbiluthyrning.repositories;

import com.example.wigellkoncernbiluthyrning.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Marc
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
