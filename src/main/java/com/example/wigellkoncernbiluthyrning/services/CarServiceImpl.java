package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.exceptions.CarDeleteException;
import com.example.wigellkoncernbiluthyrning.exceptions.AvailableCarNotFoundException;
import com.example.wigellkoncernbiluthyrning.exceptions.CarNotFoundException;
import com.example.wigellkoncernbiluthyrning.repositories.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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
        List<Car> cars = carRepository.findByBookedFalseAndInServiceFalse();
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
        CAR_LOGGER.info("A new car was added to the database");
        return carRepository.save(car);
    }
    //Anna and Nicholas
    @Override
    public Car updateCar(Car car) {
        Long carId = car.getId();

        Car existingCar = carRepository.findById(carId).orElseThrow(()-> new CarNotFoundException(carId));
        CAR_LOGGER.info("Updating existing car with id " + carId);

        existingCar.setMake(car.getMake());
        existingCar.setModel(car.getModel());
        existingCar.setPricePerDay(car.getPricePerDay());
        existingCar.setRegistrationNumber(existingCar.getRegistrationNumber());
        return carRepository.save(existingCar);

    }

    //Anna and Nicho
    @Override
    public void removeCar(Long id) {
        Car car = carRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Car with id " + id + " not found"));
        CAR_LOGGER.warn("Car with id {} was removed from the database.", id);
        if (car.isBooked()) {
            throw new CarDeleteException("Cannot delete a car with id " + id + " because it is booked");

        }
        carRepository.deleteById(id);
    }
}
