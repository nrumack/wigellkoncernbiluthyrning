package com.example.wigellkoncernbiluthyrning.services;

import java.time.LocalDate;
import java.util.Map;

// Martin
public interface StatisticsService {

    Map<String, Object> getStatistics();

    Map<String, Object> getStatisticsForPeriod(LocalDate from, LocalDate to);
}