package com.example.wigellkoncernbiluthyrning.controller;

import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.services.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok("A new order has been placed");
    }

    @PutMapping("/cancelorder")
    @ResponseBody
    public ResponseEntity<String> cancelOrder(@RequestBody Order order) {
        orderServiceImpl.cancelOrder(order);
        return ResponseEntity.ok("Your order was cancelled");
    }

    @GetMapping("/activeorders")
    @ResponseBody
    public List<Order> activeOrders() {
        return orderServiceImpl.activeorders();
    }

    @GetMapping("/orders")
    @ResponseBody
    public List<Order> orders() {
        return orderServiceImpl.orders();
    }
}
