package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Order;

import java.util.List;

// Marc
public interface OrderService {

    Order addOrder(Order order);

    Order cancelOrder(Order order);

    List<Order> activeorders();

    List<Order> orders();
}
