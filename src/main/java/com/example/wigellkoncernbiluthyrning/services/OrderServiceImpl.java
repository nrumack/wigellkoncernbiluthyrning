package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Customer;
import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.exceptions.NoContentException;
import com.example.wigellkoncernbiluthyrning.exceptions.OrderNotFoundException;
import com.example.wigellkoncernbiluthyrning.repositories.CarRepository;
import com.example.wigellkoncernbiluthyrning.repositories.CustomerRepository;
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
    private final CustomerRepository customerRepository;
    private static final Logger ORDER_LOGGER = LogManager.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CarRepository carRepository, CustomerRepository customerRepository, OrderCalculator orderCalculator) {
        this.orderRepository = orderRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.orderCalculator = orderCalculator;
    }

    @Transactional
    @Override
    public Order addOrder(Order order) {
        Customer customer = customerRepository.save(order.getCustomer());
        order.setCustomer(customer);
        orderCalculator.calculateTotalCost(order, carRepository.findCarById(order.getCar().getId()));
        ORDER_LOGGER.info("A new order was added to the database.");
        return orderRepository.save(order);
    }

    @Override
    public List<Order> activeOrders(LocalDate currentDate) {
        List<Order> activeOrders = orderRepository.findByReturnDateGreaterThanEqual(currentDate);
        if (activeOrders.isEmpty()) {
            throw new NoContentException("There are no active orders to display");
        }
        return orderRepository.findByReturnDateGreaterThanEqual(currentDate);
    }

    @Override
    public List<Order> orders(LocalDate currentDate) {
        List<Order> pastOrders = orderRepository.findOrderByReturnDateBefore(currentDate);
        if (pastOrders.isEmpty()) {
            throw new NoContentException("There were no orders to display");
        }
        return orderRepository.findOrderByReturnDateBefore(currentDate);
    }

    @Override
    public void deleteById(Long id) {
        if (orderRepository.findById(id).isEmpty()) {
            throw new OrderNotFoundException("Order with id " + id + " does not exist.");
        }
        orderRepository.deleteById(id);
        ORDER_LOGGER.warn("Order with id {} was deleted.", id);
    }

    @Transactional
    @Override
    public void deleteOrdersBeforeGivenDate(LocalDate givenDate) {
        List<Order> orders = orderRepository.findOrderByReturnDateBefore(givenDate);
        if (orders.isEmpty()) {
            throw new NoContentException("There were no orders to delete");
        }
        orderRepository.deleteOrderByReturnDateBefore(givenDate);
        ORDER_LOGGER.warn("All orders before {} have been deleted.", givenDate);
    }
}
