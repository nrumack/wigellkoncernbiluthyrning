package com.example.wigellkoncernbiluthyrning.controller;

import com.example.wigellkoncernbiluthyrning.services.CarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.wigellkoncernbiluthyrning.entities.Car;

import java.util.List;

// Anna, SCRUM-9
@Controller
@RequestMapping("/api/v1")
public class CarController {

    private final CarServiceImpl carServiceImpl;

    @Autowired
    public CarController(CarServiceImpl carServiceImpl) {
        this.carServiceImpl = carServiceImpl;
    }

    @GetMapping("/cars")
    @ResponseBody
    public List<Car> getAvailableCars() {
        return carServiceImpl.getAvailableCars();
    }
}

