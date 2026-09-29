package com.example.attendease.controller;

import com.example.attendease.service.AsyncReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/async-report")
public class AsyncReportController {

    private final AsyncReportService asyncReportService;

    public AsyncReportController(AsyncReportService asyncReportService) {
        this.asyncReportService = asyncReportService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startReport(
            @RequestParam Long subjectId) {

        System.out.println(
                "Request received on thread: "
                        + Thread.currentThread().getName()
        );

        asyncReportService.generateReportAsync(subjectId);

        return ResponseEntity.ok(
                "Attendance report generation started in background thread."
        );
    }
}