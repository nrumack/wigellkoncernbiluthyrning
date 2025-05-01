package com.example.wigellkoncernbiluthyrning.data;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.repositories.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CarData implements CommandLineRunner {
    private final CarRepository carRepository;
    public CarData(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public void run(String... args) {
        if (carRepository.count() == 0) {
            carRepository.save(new Car("Volvo", "XC60", "ABC123", 449, false, false));
            carRepository.save(new Car("Toyota", "Corolla", "XYZ789", 399.0, true, false));
            carRepository.save(new Car("BMW", "320i", "LMN456", 599.0, false, true));
            carRepository.save(new Car("Audi", "A4", "QWE987", 529.0, false, false));
            carRepository.save(new Car("Tesla", "Model 3", "TES123", 699.0, false, false));
        }

    }
}
