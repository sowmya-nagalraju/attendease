package com.example.attendease.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AsyncReportService {

    private final ReportService reportService;

    public AsyncReportService(ReportService reportService) {
        this.reportService = reportService;
    }

    // Existing threading test
    @Async
    public void generateReportAsync() {

        String threadName = Thread.currentThread().getName();

        System.out.println("========================================");
        System.out.println("Report processing started...");
        System.out.println("Running on thread: " + threadName);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Report processing was interrupted.");
        }

        System.out.println("Report processing completed.");
        System.out.println("Thread: " + threadName);
        System.out.println("========================================");
    }

    // Actual AttendEase report processing
    @Async
    public void generateReportAsync(Long subjectId) {

        String threadName = Thread.currentThread().getName();

        System.out.println("========================================");
        System.out.println("Asynchronous attendance report started");
        System.out.println("Subject ID: " + subjectId);
        System.out.println("Running on thread: " + threadName);

        try {

            List<Map<String, Object>> report =
                    reportService.getSubjectAttendanceReport(subjectId);

            System.out.println("Report generated successfully.");
            System.out.println("Number of students: " + report.size());

            for (Map<String, Object> row : report) {

                System.out.println(
                        "Student: " + row.get("studentName")
                                + " | Roll Number: " + row.get("rollNumber")
                                + " | Attendance: " + row.get("attendancePercentage") + "%"
                                + " | Shortage: " + row.get("shortage")
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Error while generating asynchronous report: "
                            + e.getMessage()
            );
        }

        System.out.println("Asynchronous attendance report completed.");
        System.out.println("Thread: " + threadName);
        System.out.println("========================================");
    }
}