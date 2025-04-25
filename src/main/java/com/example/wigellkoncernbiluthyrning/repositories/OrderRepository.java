package com.example.wigellkoncernbiluthyrning.repositories;

import com.example.wigellkoncernbiluthyrning.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Marc
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByReturnDateGreaterThanEqual(LocalDate currentDate);

    List<Order> findByReturnDateLessThan(LocalDate currentDate);

    void deleteById(@NonNull Long id);

    void deleteOrderByReturnDateBefore(LocalDate givenDate);
}
