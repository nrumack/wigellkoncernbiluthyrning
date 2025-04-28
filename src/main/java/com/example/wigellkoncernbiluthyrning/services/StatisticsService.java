package com.example.wigellkoncernbiluthyrning.services;
 
import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final OrderRepository orderRepository;

    public StatisticsService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
 
    public String getMostRentedBrand() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .collect(Collectors.groupingBy(o -> o.getCar().getMake(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No orders");
    }

    public Map<String, Long> getTotalRentalsPerCar() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .collect(Collectors.groupingBy(o -> o.getCar().getRegistrationNumber(), Collectors.counting()));
    }

    public double getAverageOrderCost() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .mapToDouble(Order::getTotalCost)
                .average()
                .orElse(0);
    }

    public int getMostCommonRentalLength() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .collect(Collectors.groupingBy(
                        o -> (int) (o.getReturnDate().toEpochDay() - o.getStartDate().toEpochDay()),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);
    }

    public Map<String, Integer> getTotalIncomePerCar() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getCar().getRegistrationNumber(),
                        Collectors.summingInt(Order::getTotalCost)
                ));
    }

    public int getTotalIncomeBetween(LocalDate from, LocalDate to) {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .filter(o -> !o.getStartDate().isBefore(from) && !o.getReturnDate().isAfter(to))
                .mapToInt(Order::getTotalCost)
                .sum();
    }
}