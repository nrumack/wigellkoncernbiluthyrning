package com.example.wigellkoncernbiluthyrning.controller;

import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.services.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

// Marc
@Controller
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderServiceImpl orderServiceImpl;

    @Autowired
    public OrderController(OrderServiceImpl orderServiceImpl) {
        this.orderServiceImpl = orderServiceImpl;
    }

    @PostMapping("/addorder")
    @ResponseBody
    public ResponseEntity<String> addOrder(@RequestBody Order order) {
        orderServiceImpl.addOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body("Your order has been placed");
    }

    @PutMapping("/cancelorder")
    public ResponseEntity<String> cancelOrder(@RequestParam Long id, Principal principal) {
        orderServiceImpl.cancelOrder(id, principal);
        return ResponseEntity.ok("Your order was cancelled");
    }

    @GetMapping("/activeorders")
    @ResponseBody
    public ResponseEntity<List<Order>> activeOrders() {
        orderServiceImpl.activeOrders(LocalDate.now());
        return ResponseEntity.ok(orderServiceImpl.activeOrders(LocalDate.now()));
    }

    @GetMapping("/orders")
    @ResponseBody
    public ResponseEntity<List<Order>> orders() {
        orderServiceImpl.orders(LocalDate.now());
        return ResponseEntity.ok(orderServiceImpl.orders(LocalDate.now()));
    }
}
