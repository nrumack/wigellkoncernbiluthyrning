package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.entities.Customer;
import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.exceptions.NoContentException;
import com.example.wigellkoncernbiluthyrning.exceptions.OrderNotFoundException;
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
import java.util.ArrayList;
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
    public OrderServiceImpl(OrderRepository orderRepository, CarRepository carRepository, OrderCalculator orderCalculator, CustomerRepository customerRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.carRepository = carRepository;
        this.orderCalculator = orderCalculator;
        this.customerRepository = customerRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    @Override
    public void addOrder(Order order) {
        Customer customer = customerRepository.findById(order.getCustomer().getId())
                .orElseThrow(() -> new IllegalArgumentException("Customer does not exist"));

        Car car = carRepository.findById(order.getCar().getId())
                .orElseThrow(() -> new IllegalArgumentException("Car does not exist"));

        order.setCustomer(customer);
        order.setCar(car);

        orderCalculator.calculateTotalCost(order, carRepository.findCarById(order.getCar().getId()));
        orderRepository.save(order);
        ORDER_LOGGER.info("A new order with id {} was added to the database.", order.getId());
    }


    @Override
    public List<Order> activeOrders(LocalDate currentDate) {
        List<Order> currentOrders = orderRepository.findByReturnDateGreaterThanEqual(currentDate);
        List<Order> activeOrders = new ArrayList<>();
        for (Order order : currentOrders) {
            if (order.isActive()) {
                activeOrders.add(order);
            }
        }
        if (activeOrders.isEmpty()) {
            throw new NoContentException("There are no active orders to display");
        }
        return activeOrders;
    }

    @Override
    public List<Order> orders(LocalDate currentDate) {
        List<Order> pastOrders = orderRepository.findOrderByReturnDateBefore(currentDate);
        if (pastOrders.isEmpty()) {
            throw new NoContentException("There were no past orders to display");
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

    @Override
    public void cancelOrder(Long id) {
        orderRepository.findById(id)
                .map(order -> {
                    order.setActive(false);
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        ORDER_LOGGER.info("Order with id {} has been cancelled", id);
    }
}
