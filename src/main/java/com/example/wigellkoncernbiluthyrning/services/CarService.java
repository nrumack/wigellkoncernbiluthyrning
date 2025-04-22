package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import java.util.List;

//Anna, SCRUM-8

public interface CarService {
    List<Car> getAllCars();
    List<Car> getAvailableCars();
    Car addCar(Car car);
    Car updateCar(Car car);
    void removeCar(Long id);
}
