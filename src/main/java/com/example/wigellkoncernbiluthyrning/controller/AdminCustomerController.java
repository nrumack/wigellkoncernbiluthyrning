package com.example.wigellkoncernbiluthyrning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/customers")
public class AdminCustomerController {

    @GetMapping
    public ResponseEntity<String> getAllCustomers() {
        return ResponseEntity.ok("Dummy: Get all customers (admin)");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getCustomerById(@PathVariable String id) {
        return ResponseEntity.ok("Dummy: Get customer with ID " + id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCustomer() {
        return ResponseEntity.ok("Dummy: Add customer");
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeCustomer(@PathVariable String id) {
        return ResponseEntity.ok("Dummy: Remove customer with ID " + id);
    }
}