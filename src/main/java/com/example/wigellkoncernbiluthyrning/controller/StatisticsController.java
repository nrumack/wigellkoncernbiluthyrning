package com.example.wigellkoncernbiluthyrning.controller;

import com.example.wigellkoncernbiluthyrning.services.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

//Martin
@RestController
@RequestMapping("/api/v1/admin/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllStatistics() {
        Map<String, Object> statistics = statisticsService.getStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<Map<String, Object>> getStatisticsForMonth(@PathVariable int year, @PathVariable int month) {

        LocalDate fromDate = LocalDate.of(year, month, 1);
        LocalDate toDate = fromDate.withDayOfMonth(fromDate.lengthOfMonth());

        Map<String, Object> statistics = statisticsService.getStatisticsForPeriod(fromDate, toDate);
        return ResponseEntity.ok(statistics);
    }
}