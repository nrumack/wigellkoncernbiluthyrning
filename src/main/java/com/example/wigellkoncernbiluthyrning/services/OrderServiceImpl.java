package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.entities.Customer;
import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.exceptions.IncorrectFormException;
import com.example.wigellkoncernbiluthyrning.exceptions.NoContentException;
import com.example.wigellkoncernbiluthyrning.exceptions.OrderNotFoundException;
import com.example.wigellkoncernbiluthyrning.exceptions.UnauthorisedUserException;
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

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        String message = orderValidator.orderForm(order);
        if (!orderValidator.isValid()) {
            throw new IncorrectFormException(message);
        }
            Customer customer = customerRepository.findById(order.getCustomer().getId())
                    .orElseThrow(() -> new IncorrectFormException("Customer does not exist"));
            Car car = carRepository.findById(order.getCar().getId())
                    .orElseThrow(() -> new IncorrectFormException("Car does not exist"));
            order.setCustomer(customer);
            order.setCar(car);
            orderCalculator.calculateTotalCost(order, carRepository.findCarById(order.getCar().getId()));
            orderRepository.save(order);
            ORDER_LOGGER.info("A new order with id {} was added to the database.", order.getId());
    }

    @Override
    public List<Order> activeOrders(LocalDate currentDate, Principal principal) {
        List<Order> currentOrders = orderRepository.findByReturnDateGreaterThanEqual(currentDate);
        List<Order> activeOrders = new ArrayList<>();
        String userName = principal.getName();
        for (Order order : currentOrders) {
            if (order.isActive() && order.getCustomer().getSocialSecurityNumber().equals(userName)) {
                activeOrders.add(order);
            }
        }
        if (activeOrders.isEmpty()) {
            throw new NoContentException("There are no active orders to display");
        }
        return activeOrders;
    }

    @Override
    public List<Order> orders(LocalDate currentDate, Principal principal) {
        List<Order> pastOrders = orderRepository.findOrderByReturnDateBefore(currentDate);
        List<Order> filteredOrderList = new ArrayList<>();
        String userName = principal.getName();
        for (Order order : pastOrders) {
            if (order.getCustomer().getSocialSecurityNumber().equals(userName)) {
                filteredOrderList.add(order);
            }
        }
        if (pastOrders.isEmpty()) {
            throw new NoContentException("There were no past orders to display");
        }
        return filteredOrderList;
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
    public void cancelOrder(Long id, Principal principal) {
        Optional<Order> checkedOrder = orderRepository.findById(id);
        String loginName = principal.getName();
        Order order = checkedOrder.orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!order.getCustomer().getSocialSecurityNumber().equals(loginName)) {
            throw new UnauthorisedUserException("You are not authorised to cancel this order");
        }
        order.setActive(false);
        orderRepository.save(order);
        ORDER_LOGGER.info("Order with id {} has been cancelled", id);
    }
}
