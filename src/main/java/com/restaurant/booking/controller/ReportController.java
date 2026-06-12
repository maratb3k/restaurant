package com.restaurant.booking.controller;

import com.restaurant.booking.model.entity.Reports;
import com.restaurant.booking.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reports")
public class ReportController {
    private final ReportService service;


    @PostMapping("/generate")
    public ResponseEntity<Reports> generate(@RequestParam String performanceType,
                                            @RequestParam String startTime,
                                            @RequestParam String endTime,
                                            @RequestParam String locationId){
        Reports reportMetrics = service.generateReports(performanceType, startTime, endTime, locationId);
        return ResponseEntity.ok(reportMetrics);
    }
}
