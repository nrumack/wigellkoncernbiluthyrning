package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.repositories.CarRepository;
import com.example.wigellkoncernbiluthyrning.repositories.OrderRepository;
import com.example.wigellkoncernbiluthyrning.services.calculators.OrderCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

// Marc
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CarRepository carRepository;
    private final OrderCalculator orderCalculator;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CarRepository carRepository) {
        this.orderRepository = orderRepository;
        this.carRepository = carRepository;
        this.orderCalculator = new OrderCalculator();
    }

    @Override
    public Order addOrder(Order order) {
        //orderCalculator.calculateTotalCost(order, carRepository.getCarById(order.getCar().getId()));
        return orderRepository.save(order);
    }

    @Override
    public List<Order> activeOrders(LocalDate currentDate) {
        return orderRepository.findByReturnDateGreaterThanEqual(currentDate);
    }

    @Override
    public List<Order> orders(LocalDate currentDate) {
        return orderRepository.findByReturnDateLessThan(currentDate);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteOrdersBeforeGivenDate(LocalDate givenDate) {
        orderRepository.deleteOrderByReturnDateBefore(givenDate);
    }
}
