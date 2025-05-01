package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.repositories.CarRepository;
import com.example.wigellkoncernbiluthyrning.repositories.OrderRepository;
import com.example.wigellkoncernbiluthyrning.services.calculators.OrderCalculator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger ORDER_LOGGER = LogManager.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CarRepository carRepository) {
        this.orderRepository = orderRepository;
        this.carRepository = carRepository;
        this.orderCalculator = new OrderCalculator();
    }

    @Override
    public Order addOrder(Order order) {
        //ORDER_LOGGER.warn("The user tried entering an invalid/incomplete order");
        orderCalculator.calculateTotalCost(order, carRepository.findCarById(order.getCar().getId()));
        ORDER_LOGGER.info("A new order was placed.");
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
        ORDER_LOGGER.warn("An order with id {} was deleted.", id);
        orderRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteOrdersBeforeGivenDate(LocalDate givenDate) {
        ORDER_LOGGER.warn("All orders before {} have been deleted.", givenDate);
        orderRepository.deleteOrderByReturnDateBefore(givenDate);
    }
}
