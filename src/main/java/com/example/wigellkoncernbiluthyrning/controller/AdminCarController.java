package com.example.wigellkoncernbiluthyrning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/cars")
public class AdminCarController {

    @GetMapping
    public ResponseEntity<String> getAvailableCars() {
        return ResponseEntity.ok("Dummy: Get available cars");
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAllCars() {
        return ResponseEntity.ok("Dummy: Get all cars");
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCar() {
        return ResponseEntity.ok("Dummy: Add car");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCar() {
        return ResponseEntity.ok("Dummy: Update car");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeCar() {
        return ResponseEntity.ok("Dummy: Remove car");
    }
}