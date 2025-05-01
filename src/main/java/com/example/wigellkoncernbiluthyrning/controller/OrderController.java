package com.example.wigellkoncernbiluthyrning.controller;

import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.repositories.OrderRepository;
import com.example.wigellkoncernbiluthyrning.services.OrderServiceImpl;
import com.example.wigellkoncernbiluthyrning.validators.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// Marc
@Controller
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderServiceImpl orderServiceImpl;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    @Autowired
    public OrderController(OrderServiceImpl orderServiceImpl, OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderServiceImpl = orderServiceImpl;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @PostMapping("/addorder")
    @ResponseBody
    public ResponseEntity<String> addOrder(@RequestBody Order order) {
        String message = orderValidator.orderForm(order);
        if (!orderValidator.isValid()) {
            return ResponseEntity.badRequest().body(message);
        }
        orderServiceImpl.addOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body("Your order has been placed");
    }

    @PutMapping("/cancelorder")
    public ResponseEntity<String> cancelOrder(@RequestParam Long id) {
        orderServiceImpl.deleteById(id);
        return ResponseEntity.ok("Your order was cancelled");
    }

    @GetMapping("/activeorders")
    @ResponseBody
    public ResponseEntity<?> activeOrders() {
        return ResponseEntity.ok(orderServiceImpl.activeOrders(LocalDate.now()));
    }

    @GetMapping("/orders")
    @ResponseBody
    public ResponseEntity<?> orders() {
        orderServiceImpl.orders(LocalDate.now());
        return ResponseEntity.ok(orderServiceImpl.orders(LocalDate.now()));
    }
}
