package com.example.wigellkoncernbiluthyrning.controller;

import com.example.wigellkoncernbiluthyrning.services.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllStatistics() {

        Map<String, Object> stats = new LinkedHashMap<>();

        stats.put("mostRentedBrand", statisticsService.getMostRentedBrand());
        stats.put("totalRentalsPerCar", statisticsService.getTotalRentalsPerCar());
        stats.put("averageOrderCost", statisticsService.getAverageOrderCost());
        stats.put("mostCommonRentalLength", statisticsService.getMostCommonRentalLength());
        stats.put("totalIncomePerCar", statisticsService.getTotalIncomePerCar());
        stats.put("totalIncome", statisticsService.getTotalIncome());

        return ResponseEntity.ok(stats);
    }
}