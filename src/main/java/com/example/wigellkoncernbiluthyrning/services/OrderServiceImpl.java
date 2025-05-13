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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public void addOrder(Order order, Principal principal) {
        orderValidator.validateOrder(order);
        Customer customer = customerRepository.findById(order.getCustomer().getId())
                .orElseThrow(() -> new IncorrectFormException("Customer does not exist in the database"));
        if (!Objects.equals(customer.getSocialSecurityNumber(), principal.getName())) {
            throw new UnauthorisedUserException("You cannot make a booking for someone else");
        }
        Car car = carRepository.findById(order.getCar().getId())
                .orElseThrow(() -> new IncorrectFormException("Car does not exist in the database"));
        if (orderRepository.isCarBookedDuringPeriod(car, order.getStartDate(), order.getReturnDate())) {
            throw new IncorrectFormException("Car is already booked during this period");
        }
        order.setCustomer(customer);
        order.setCar(car);
        orderCalculator.calculateTotalCost(order, carRepository.findCarById(order.getCar().getId()));
        orderRepository.save(order);
        ORDER_LOGGER.info("[{} {}, id:{}] added [Order, id:{}] to the database.",
                order.getCustomer().getFirstName(), order.getCustomer().getLastName(),
                order.getCustomer().getId(), order.getId());
    }

    @Override
    public List<Order> activeOrders(LocalDate currentDate, Principal principal) {
        List<Order> currentOrders = orderRepository.findByReturnDateGreaterThanEqual(currentDate);
        List<Order> activeOrders = new ArrayList<>();
        String userName = principal.getName();

        boolean isAdmin = userName.equals("admin");

        for (Order order : currentOrders) {
            if (!order.isCancelled() && (isAdmin || order.getCustomer().getSocialSecurityNumber().equals(userName))) {
                activeOrders.add(order);
            }
        }
        if (activeOrders.isEmpty()) {
            throw new NoContentException("There are no active orders to display");
        }
        return activeOrders;
    }

    @Override
    public List<Order> pastOrders(LocalDate currentDate, Principal principal) {
        List<Order> allPastOrders = orderRepository.findOrderByReturnDateBefore(currentDate);
        String userName = principal.getName();

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            if (allPastOrders.isEmpty()) {
                throw new NoContentException("There were no orders to display");
            }
            return allPastOrders;
        }

        List<Order> filteredOrderList = new ArrayList<>();
        for (Order order : allPastOrders) {
            if (order.getCustomer().getSocialSecurityNumber().equals(userName)) {
                filteredOrderList.add(order);
            }
        }
        if (filteredOrderList.isEmpty()) {
            throw new NoContentException("There were no orders to display");
        }
        return filteredOrderList;
    }

    @Override
    public void deleteById(Long id) {
        if (orderRepository.findById(id).isEmpty()) {
            throw new OrderNotFoundException("Order with id " + id + " does not exist.");
        }
        orderRepository.deleteById(id);
        ORDER_LOGGER.info("Order with id:{} was deleted.", id);
    }

    @Transactional
    @Override
    public void deleteOrdersBeforeGivenDate(LocalDate givenDate) {
        List<Order> orders = orderRepository.findOrderByReturnDateBefore(givenDate);
        if (orders.isEmpty()) {
            throw new NoContentException("There were no orders to delete");
        }
        orderRepository.deleteOrderByReturnDateBefore(givenDate);
        ORDER_LOGGER.info("All orders before {} have been deleted.", givenDate);
    }

    @Override
    public void cancelOrder(Long id, Principal principal) {
        Optional<Order> checkedOrder = orderRepository.findById(id);
        String loginName = principal.getName();
        Order order = checkedOrder.orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!order.getCustomer().getSocialSecurityNumber().equals(loginName)) {
            throw new UnauthorisedUserException("You are not authorised to cancel this order");
        }
        order.setCancelled(true);
        orderRepository.save(order);
        ORDER_LOGGER.info("Order with id:{} has been cancelled", id);
    }
}
