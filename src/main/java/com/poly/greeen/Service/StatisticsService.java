package com.poly.greeen.Service;

import com.poly.greeen.Repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public Map<String, Integer> getEntityCounts() {
        List<Object[]> results = statisticsRepository.getEntityCounts();
        Map<String, Integer> entityCounts = new HashMap<>();

        entityCounts.put("UserCount", ((Number) results.get(0)[0]).intValue());
        entityCounts.put("CustomerCount", ((Number) results.get(0)[1]).intValue());
        entityCounts.put("ProductCount", ((Number) results.get(0)[2]).intValue());

        return entityCounts;
    }

    public List<Map<String, Object>> getFinancialStatisticsByYear() {
        List<Object[]> results = statisticsRepository.getFinancialStatisticsByYear();
        List<Map<String, Object>> financialStats = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> yearStats = new HashMap<>();
            yearStats.put("Year", row[0]);
            yearStats.put("TotalOrderAmount", row[1]);
            yearStats.put("TotalDailyWage", row[2]);
            yearStats.put("TotalImportAmount", row[3]);
            financialStats.add(yearStats);
        }

        return financialStats;
    }
}
