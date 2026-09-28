package com.example.attendease.controller;

import com.example.attendease.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/attendance")
    public double getAttendancePercentage(
            @RequestParam Long studentId,
            @RequestParam Long subjectId) {

        return reportService.calculateAttendancePercentage(
                studentId,
                subjectId
        );
    }

    @GetMapping("/shortage/check")
    public boolean checkShortage(
            @RequestParam Long studentId,
            @RequestParam Long subjectId) {

        return reportService.hasAttendanceShortage(
                studentId,
                subjectId
        );
    }

    @GetMapping("/subject/{subjectId}")
    public List<Map<String, Object>> getSubjectReport(
            @PathVariable Long subjectId) {

        return reportService.getSubjectAttendanceReport(
                subjectId
        );
    }

    @GetMapping("/shortage/{subjectId}")
    public List<Map<String, Object>> getShortageStudents(
            @PathVariable Long subjectId) {

        return reportService.getShortageStudents(
                subjectId
        );
    }
}