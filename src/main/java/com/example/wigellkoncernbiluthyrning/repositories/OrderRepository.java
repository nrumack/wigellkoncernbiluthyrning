package com.example.wigellkoncernbiluthyrning.repositories;

import com.example.wigellkoncernbiluthyrning.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Marc
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrderByReturnDateBefore(LocalDate currentDate);
}
