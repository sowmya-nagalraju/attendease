package com.example.attendease.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AttendanceRequest {

    @NotBlank(message = "Roll number is required")
    private String rollNumber;

    @NotNull(message = "Session ID is required")
    private Long sessionId;

    @NotNull(message = "Attendance status is required")
    private Boolean present;

    public AttendanceRequest() {
    }

    public AttendanceRequest(
            String rollNumber,
            Long sessionId,
            Boolean present) {

        this.rollNumber = rollNumber;
        this.sessionId = sessionId;
        this.present = present;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Boolean getPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }
}