package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;


//Martin
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final OrderRepository orderRepository;

    public StatisticsServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new LinkedHashMap<>();
        List<Order> orders = orderRepository.findAll();

        stats.put("mostRentedBrand", getMostRentedBrandFromOrders(orders));
        stats.put("mostRentedModel", getMostRentedModelFromOrders(orders));
        stats.put("averageRentalLengthInDays", getAverageRentalLength(orders));
        stats.put("longestRentalDurationInDays", getLongestRentalDuration(orders));
        stats.put("totalOrdersThisYear", getTotalOrdersThisYear(orders));
        stats.put("averageOrderCost", getAverageOrderCost(orders));
        stats.put("totalRevenue", orders.stream().mapToInt(Order::getTotalCost).sum());
        stats.put("incomePerCar", getTotalIncomePerCarFromOrders(orders));
        stats.put("incomePerBrand", getTotalIncomePerBrand(orders));
        stats.put("monthlyRevenueLast12Months", getMonthlyRevenue(orders));
        stats.put("topCustomerBySpending", getTopCustomerBySpending(orders));

        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStatisticsForPeriod(LocalDate from, LocalDate to) {
        List<Order> orders = orderRepository.findAll().stream()
                .filter(o -> !o.getStartDate().isBefore(from) && !o.getStartDate().isAfter(to))
                .toList();

        Map<String, Object> stats = new LinkedHashMap<>();

        stats.put("totalIncome", orders.stream().mapToInt(Order::getTotalCost).sum());
        stats.put("averageOrderCost", getAverageOrderCost(orders));
        stats.put("totalOrdersInPeriod", orders.size());
        stats.put("totalRentalsPerCar", getTotalOrdersPerCar(orders));
        stats.put("totalIncomePerCar", getTotalIncomePerCarFromOrders(orders));

        return stats;
    }

    private String getMostRentedBrandFromOrders(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(o -> o.getCar().getMake(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No orders");
    }

    private String getMostRentedModelFromOrders(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(o -> o.getCar().getModel(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No orders");
    }

    private Map<String, Long> getTotalOrdersPerCar(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(o -> o.getCar().getRegistrationNumber(), Collectors.counting()));
    }

    private Map<String, Object> getTopCustomerBySpending(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(o -> o.getCustomer().getSocialSecurityNumber() + " - " + o.getCustomer().getFirstName() + " " + o.getCustomer().getLastName(), Collectors.summingInt(Order::getTotalCost)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> Map.of("customer", e.getKey(), "totalSpent", (Object) e.getValue()))
                .orElseGet(() -> new LinkedHashMap<>());
    }

    private double getAverageRentalLength(List<Order> orders) {
        return orders.stream()
                .mapToLong(o -> o.getReturnDate().toEpochDay() - o.getStartDate().toEpochDay())
                .average()
                .orElse(0);
    }

    private double getAverageOrderCost(List<Order> orders) {
        return orders.stream()
                .mapToInt(Order::getTotalCost)
                .average()
                .orElse(0);
    }

    private long getTotalOrdersThisYear(List<Order> orders) {
        return orders.stream()
                .filter(o -> o.getOrderPlaced().getYear() == LocalDate.now().getYear())
                .count();
    }

    private Map<String, Integer> getTotalIncomePerCarFromOrders(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(o -> o.getCar().getRegistrationNumber(), Collectors.summingInt(Order::getTotalCost)));
    }

    private Map<String, Integer> getTotalIncomePerBrand(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(o -> o.getCar().getMake(), Collectors.summingInt(Order::getTotalCost)));
    }

    private int getLongestRentalDuration(List<Order> orders) {
        return orders.stream()
                .mapToInt(o -> (int) (o.getReturnDate().toEpochDay() - o.getStartDate().toEpochDay()))
                .max()
                .orElse(0);
    }

    private Map<String, Integer> getMonthlyRevenue(List<Order> orders) {
        Map<String, Integer> monthlyRevenue = new LinkedHashMap<>();
        for (int i = 0; i < 12; i++) {
            YearMonth ym = YearMonth.now().minusMonths(i);
            int sum = orders.stream()
                    .filter(o -> YearMonth.from(o.getOrderPlaced()).equals(ym))
                    .mapToInt(Order::getTotalCost)
                    .sum();
            monthlyRevenue.put(ym.toString(), sum);
        }
        return monthlyRevenue;
    }
}