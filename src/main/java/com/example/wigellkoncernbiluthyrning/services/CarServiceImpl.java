package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.repositories.CarRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//Anna, SCRUM-8

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private static final Logger CAR_LOGGER = LogManager.getLogger(CarServiceImpl.class);

    @Autowired
    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> getAvailableCars() {
        return carRepository.findByBookedFalseAndInServiceFalse();
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Car addCar(Car car) {
        CAR_LOGGER.info("A new car was added to the database");
        return carRepository.save(car);
    }

    @Override
    public Car updateCar(Car car) {
        Optional<Car> existing = carRepository.findById(car.getId());
        if (existing.isPresent()) {
            CAR_LOGGER.info("Details for car with id {} have been updated.", existing);
            return carRepository.save(car);
        } else {
            CAR_LOGGER.warn("Admin tried updating a car with id {} but it does not exist in the database.", existing);
            throw new RuntimeException("Car with ID " + car.getId() + " not found");
        }
    }

    @Override
    public void removeCar(Long id) {
        if (carRepository.existsById(id)) {
            CAR_LOGGER.warn("Car with id {} was removed from the database.", id);
            carRepository.deleteById(id);
        } else {
            CAR_LOGGER.warn("Admin tried deleting a car with id {} but it does not exist in the database", id);
            throw new RuntimeException("Car with ID " + id + " not found");
        }
    }
}
