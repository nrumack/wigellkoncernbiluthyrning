package com.example.wigellkoncernbiluthyrning.services.calculators;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.entities.Order;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

// Marc
@Component
public class OrderCalculator {

    public void calculateTotalCost(Order order, Car car) {
        long daysRented = ChronoUnit.DAYS.between(order.getStartDate(), order.getReturnDate()) + 1;
        order.setTotalCost((int) (daysRented * car.getPricePerDay()));
    }
}
