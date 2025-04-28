package com.example.wigellkoncernbiluthyrning.controller;

import com.example.wigellkoncernbiluthyrning.services.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.time.LocalDate;

// Martin
@RestController
@RequestMapping("/api/v1/admin/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/most-rented-brand")
    public ResponseEntity<String> getMostRentedBrand() {
        return ResponseEntity.ok(statisticsService.getMostRentedBrand());
    }

    @GetMapping("/total-income")
    public ResponseEntity<Integer> getTotalIncome(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(statisticsService.getTotalIncomeBetween(from, to));
    }

        @GetMapping("/rentals-per-car")
    public ResponseEntity<Map<String, Long>> getRentalsPerCar() {
        return ResponseEntity.ok(statisticsService.getTotalRentalsPerCar());
    }

    @GetMapping("/average-cost")
    public ResponseEntity<Double> getAverageOrderCost() {
        return ResponseEntity.ok(statisticsService.getAverageOrderCost());
    }

    @GetMapping("/common-rental-length")
    public ResponseEntity<Integer> getMostCommonRentalLength() {
        return ResponseEntity.ok(statisticsService.getMostCommonRentalLength());
    }

    @GetMapping("/income-per-car")
    public ResponseEntity<Map<String, Integer>> getTotalIncomePerCar() {
        return ResponseEntity.ok(statisticsService.getTotalIncomePerCar());
    }
}