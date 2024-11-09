package com.poly.greeen.Controller;

import com.poly.greeen.Service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@Slf4j
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/entity-counts")
    public Map<String, Integer> getEntityCounts() {
        return statisticsService.getEntityCounts();
    }

    @GetMapping("/financial-stats")
    public List<Map<String, Object>> getFinancialStatisticsByYear() {
        return statisticsService.getFinancialStatisticsByYear();
    }
}
