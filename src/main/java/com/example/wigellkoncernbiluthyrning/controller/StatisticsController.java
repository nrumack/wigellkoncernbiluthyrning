package com.example.wigellkoncernbiluthyrning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/statistics")
public class StatisticsController {

    @GetMapping
    public ResponseEntity<String> getStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok("Statistics for period: " + startDate + " to " + endDate);
    }
}