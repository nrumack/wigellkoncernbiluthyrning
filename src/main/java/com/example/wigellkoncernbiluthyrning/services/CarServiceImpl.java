package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.exceptions.AvailableCarNotFoundException;
import com.example.wigellkoncernbiluthyrning.exceptions.CarDeleteException;
import com.example.wigellkoncernbiluthyrning.exceptions.CarNotFoundException;
import com.example.wigellkoncernbiluthyrning.repositories.CarRepository;
import com.example.wigellkoncernbiluthyrning.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Anna, SCRUM-8
@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, OrderRepository orderRepository, OrderService orderService) {
        this.carRepository = carRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @Override
    public List<Car> getAvailableCars() {
        List<Car> cars = carRepository.findAll();
        if (cars.isEmpty()) {
            throw new AvailableCarNotFoundException();
        }
        return cars;
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Car addCar(Car car) {
        // Anna, SCRUM-8
        return carRepository.save(car);
    }

    // Anna and Nicho
    @Override
    public Car updateCar(Car car) {
        Long carId = car.getId();
        Car existingCar = carRepository.findById(carId).orElseThrow(() -> new CarNotFoundException(carId));

        existingCar.setMake(car.getMake());
        existingCar.setModel(car.getModel());
        existingCar.setPricePerDay(car.getPricePerDay());
        existingCar.setRegistrationNumber(existingCar.getRegistrationNumber());

        return carRepository.save(existingCar);
    }

    // Anna and Nicho
    @Override
    public void removeCar(Long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Car with id " + id + " not found"));

        LocalDate today = LocalDate.now();
        boolean isBooked = orderRepository.isCarBookedToday(car, today);
        if (isBooked) {
            throw new CarDeleteException("Cannot delete a car with id " + id + " because it is currently booked");
        }

        carRepository.deleteById(id);
    }

    //Martin
    @Override
    public List<Car> getAvailableCarsToday() {
        List<Car> cars = carRepository.findAll();
        List<Car> availableCars = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Car car : cars) {
            boolean isBooked = orderRepository.isCarBookedToday(car, today);
            if (!isBooked) {
                availableCars.add(car);
            }
        }

        return availableCars;
    }
}