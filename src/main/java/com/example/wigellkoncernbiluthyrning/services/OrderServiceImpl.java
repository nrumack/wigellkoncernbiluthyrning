package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.exceptions.BasicException;
import com.example.wigellkoncernbiluthyrning.repositories.CarRepository;
import com.example.wigellkoncernbiluthyrning.repositories.CustomerRepository;
import com.example.wigellkoncernbiluthyrning.repositories.OrderRepository;
import com.example.wigellkoncernbiluthyrning.services.calculators.OrderCalculator;
import com.example.wigellkoncernbiluthyrning.validators.OrderValidator;
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
    private final CustomerRepository customerRepository;
    private final OrderValidator orderValidator;
    private static final Logger ORDER_LOGGER = LogManager.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CarRepository carRepository, CustomerRepository customerRepository, OrderCalculator orderCalculator, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.orderCalculator = orderCalculator;
        this.orderValidator = orderValidator;
    }

    @Transactional
    @Override
    public Order addOrder(Order order) {
        /*Customer customer = customerRepository.save(order.getCustomer());
        order.setCustomer(customer);*/
        orderCalculator.calculateTotalCost(order, carRepository.findCarById(order.getCar().getId()));
        ORDER_LOGGER.info("A new order was added to the database.");
        return orderRepository.save(order);
    }

    @Override
    public List<Order> activeOrders(LocalDate currentDate) {
        if (orderRepository.findByReturnDateGreaterThanEqual(currentDate).isEmpty()) {
            throw new BasicException("There are no active orders");
        }
        return orderRepository.findByReturnDateGreaterThanEqual(currentDate);
    }

    @Override
    public List<Order> orders(LocalDate currentDate) {
        return orderRepository.findByReturnDateLessThan(currentDate);
    }

    @Override
    public void deleteById(Long id) {
        if (orderRepository.findById(id).isEmpty()) {
            throw new BasicException("No order with id " + id + " was found.");
        }
        ORDER_LOGGER.warn("Order with id {} was deleted.", id);
        orderRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteOrdersBeforeGivenDate(LocalDate givenDate) {
        ORDER_LOGGER.warn("All orders before {} have been deleted.", givenDate);
        orderRepository.deleteOrderByReturnDateBefore(givenDate);
    }
}
