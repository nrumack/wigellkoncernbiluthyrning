package com.example.wigellkoncernbiluthyrning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/orders")
public class AdminOrderController {

    @GetMapping("/active")
    public ResponseEntity<String> getActiveOrders() {
        return ResponseEntity.ok("Dummy: Get all active orders");
    }

    @GetMapping("/history")
    public ResponseEntity<String> getHistoricalOrders() {
        return ResponseEntity.ok("Dummy: Get all past orders");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeOrder() {
        return ResponseEntity.ok("Dummy: Remove specific order");
    }

    @DeleteMapping("/remove-beforedate/{date}")
    public ResponseEntity<String> removeOrdersBeforeDate(@PathVariable String date) {
        return ResponseEntity.ok("Dummy: Remove orders before " + date);
    }
}