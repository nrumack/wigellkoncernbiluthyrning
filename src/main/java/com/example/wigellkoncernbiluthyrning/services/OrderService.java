package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Order;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

// Marc
public interface OrderService {

    void addOrder(Order order);

    List<Order> activeOrders(LocalDate currentDate);

    List<Order> orders(LocalDate currentDate);

    void deleteById(Long id);

    void deleteOrdersBeforeGivenDate(LocalDate givenDate);

    void cancelOrder(Long id, Principal principal);
}
