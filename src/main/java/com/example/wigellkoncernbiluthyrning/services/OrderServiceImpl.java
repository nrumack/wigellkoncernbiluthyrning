package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// Marc
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> activeorders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> orders(LocalDate currentDate) {
        return orderRepository.findOrderByReturnDateBefore(currentDate);
    }
}
