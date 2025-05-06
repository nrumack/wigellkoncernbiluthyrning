package com.example.wigellkoncernbiluthyrning.controller;

import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

//Martin
@RestController
@RequestMapping("/api/v1/admin")
public class AdminOrderController {

    private final OrderService orderService;

    @Autowired
    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/activeorders")
    public ResponseEntity<List<Order>> getActiveOrders(Principal principal) {
        return ResponseEntity.ok(orderService.activeOrders(LocalDate.now(), principal));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getPastOrders(Principal principal) {
        return ResponseEntity.ok(orderService.orders(LocalDate.now(), principal));
    }

    @DeleteMapping("/removeorder/{id}")
    public ResponseEntity<String> removeOrder(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.ok("Order removed successfully");
    }

    @DeleteMapping("/removeorders-beforedate/{date}")
    public ResponseEntity<String> removeOrdersBeforeDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        orderService.deleteOrdersBeforeGivenDate(date);
        return ResponseEntity.ok("Orders before " + date + " have been deleted");
    }
}