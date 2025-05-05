package com.example.wigellkoncernbiluthyrning.controller;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Martin
@RestController
@RequestMapping("/api/v1/admin")
public class AdminCarController {

    private final CarService carService;

    @Autowired
    public AdminCarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getAvailableCars() {
        return ResponseEntity.ok(carService.getAvailableCars());
    }

    @GetMapping("/allcars")
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @PostMapping("/addcar")
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        return ResponseEntity.ok(carService.addCar(car));
    }

    @PutMapping("/updatecar/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car carInfo) {
        carInfo.setId(id);
        Car updatedCar = carService.updateCar(carInfo);
        return ResponseEntity.ok(updatedCar);
    }

    @DeleteMapping("/removecar/{id}")
    public ResponseEntity<String> removeCar(@PathVariable Long id) {
        carService.removeCar(id);
        return ResponseEntity.ok("Car removed successfully");
    }
}