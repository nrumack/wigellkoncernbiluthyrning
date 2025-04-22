package com.example.wigellkoncernbiluthyrning.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final List<Order> dummyOrders;

    public StatisticsService() {
        Customer anna = new Customer("19850101-1234", "Martin", "Svensson");
        Customer erik = new Customer("19900215-5678", "Erik", "Johansson");

        Car volvo = new Car("ABC123", "Volvo", "XC60", 799);
        Car bmw = new Car("DEF456", "BMW", "320i", 899);

        Order o1 = new Order(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 4), volvo, anna); 
        o1.setTotalCost(o1.calculateTotalCost());

        Order o2 = new Order(LocalDate.of(2025, 2, 10), LocalDate.of(2025, 2, 15), volvo, erik); 

        Order o3 = new Order(LocalDate.of(2025, 2, 20), LocalDate.of(2025, 2, 23), bmw, anna);
        o3.setTotalCost(o3.calculateTotalCost());

        dummyOrders = List.of(o1, o2, o3);
    }

    public String getMostRentedBrand() {
        return dummyOrders.stream()
                .collect(Collectors.groupingBy(o -> o.getCar().getBrand(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No orders");
    }

    public Map<String, Long> getTotalRentalsPerCar() {
        return dummyOrders.stream()
                .collect(Collectors.groupingBy(o -> o.getCar().getRegistrationNumber(), Collectors.counting()));
    }

    public double getAverageOrderCost() {
        return dummyOrders.stream()
                .mapToDouble(Order::getTotalCost)
                .average()
                .orElse(0);
    }

    public int getMostCommonRentalLength() {
        return dummyOrders.stream()
                .collect(Collectors.groupingBy(
                        o -> (int) (o.getEndDate().toEpochDay() - o.getStartDate().toEpochDay()),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);
    }

    public Map<String, Integer> getTotalIncomePerCar() {
        return dummyOrders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getCar().getRegistrationNumber(),
                        Collectors.summingInt(Order::getTotalCost)
                ));
    }

    public int getTotalIncomeBetween(LocalDate from, LocalDate to) {
        return dummyOrders.stream()
                .filter(o -> !o.getStartDate().isBefore(from) && !o.getEndDate().isAfter(to))
                .mapToInt(Order::getTotalCost)
                .sum();
    }

    // === Dummy pojo classes ===

    static class Customer {
        private String personnummer;
        private String firstName;
        private String lastName;

        public Customer(String personnummer, String firstName, String lastName) {
            this.personnummer = personnummer;
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    static class Car {
        private String registrationNumber;
        private String brand;
        private String model;
        private int pricePerDay;

        public Car(String registrationNumber, String brand, String model, int pricePerDay) {
            this.registrationNumber = registrationNumber;
            this.brand = brand;
            this.model = model;
            this.pricePerDay = pricePerDay;
        }

        public String getRegistrationNumber() {
            return registrationNumber;
        }

        public String getBrand() {
            return brand;
        }

        public int getPricePerDay() {
            return pricePerDay;
        }
    }

    static class Order {
        private LocalDate startDate;
        private LocalDate endDate;
        private Car car;
        private Customer customer;
        private int totalCost;

        public Order(LocalDate startDate, LocalDate endDate, Car car, Customer customer) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.car = car;
            this.customer = customer;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public Car getCar() {
            return car;
        }

        public int getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(int totalCost) {
            this.totalCost = totalCost;
        }

        public int calculateTotalCost() {
            long days = endDate.toEpochDay() - startDate.toEpochDay();
            return (int) days * car.getPricePerDay();
        }
    }
}