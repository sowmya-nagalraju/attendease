package com.example.attendease.controller;

import com.example.attendease.dto.AttendanceRequest;
import com.example.attendease.entity.AttendanceRecord;
import com.example.attendease.exception.ResourceNotFoundException;
import com.example.attendease.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(
            AttendanceService attendanceService) {

        this.attendanceService = attendanceService;
    }

    @GetMapping
    public List<AttendanceRecord> getAllAttendance() {
        return attendanceService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceRecord> getAttendanceById(
            @PathVariable Long id) {

        return attendanceService.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attendance record not found with id: "
                                        + id
                        ));
    }

    @GetMapping("/student/{studentId}")
    public List<AttendanceRecord> getAttendanceByStudent(
            @PathVariable Long studentId) {

        return attendanceService.getByStudent(studentId);
    }

    @GetMapping("/session/{sessionId}")
    public List<AttendanceRecord> getAttendanceBySession(
            @PathVariable Long sessionId) {

        return attendanceService.getBySession(sessionId);
    }

    @PostMapping
    public ResponseEntity<AttendanceRecord> markAttendance(
            @Valid @RequestBody AttendanceRequest request) {

        AttendanceRecord record =
                attendanceService.markAttendance(
                        request.getRollNumber(),
                        request.getSessionId(),
                        request.getPresent()
                );

        return ResponseEntity.ok(record);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceRecord> correctAttendance(
            @PathVariable Long id,
            @RequestParam Boolean present) {

        AttendanceRecord updatedRecord =
                attendanceService.correctAttendance(
                        id,
                        present
                );

        return ResponseEntity.ok(updatedRecord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(
            @PathVariable Long id) {

        if (attendanceService.getById(id).isEmpty()) {
            throw new ResourceNotFoundException(
                    "Attendance record not found with id: " + id
            );
        }

        attendanceService.delete(id);

        return ResponseEntity.noContent().build();
    }
}