package com.example.attendease.dto;

public class AttendanceSummary {

    private Long studentId;
    private String rollNumber;
    private String studentName;

    private Long subjectId;
    private String subjectCode;
    private String subjectName;

    private int totalSessions;
    private int presentSessions;
    private int absentSessions;

    private double attendancePercentage;
    private double minimumAttendancePercentage;

    private boolean shortage;

    public AttendanceSummary() {
    }

    public AttendanceSummary(
            Long studentId,
            String rollNumber,
            String studentName,
            Long subjectId,
            String subjectCode,
            String subjectName,
            int totalSessions,
            int presentSessions,
            int absentSessions,
            double attendancePercentage,
            double minimumAttendancePercentage,
            boolean shortage) {

        this.studentId = studentId;
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.subjectId = subjectId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.totalSessions = totalSessions;
        this.presentSessions = presentSessions;
        this.absentSessions = absentSessions;
        this.attendancePercentage = attendancePercentage;
        this.minimumAttendancePercentage = minimumAttendancePercentage;
        this.shortage = shortage;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public int getPresentSessions() {
        return presentSessions;
    }

    public void setPresentSessions(int presentSessions) {
        this.presentSessions = presentSessions;
    }

    public int getAbsentSessions() {
        return absentSessions;
    }

    public void setAbsentSessions(int absentSessions) {
        this.absentSessions = absentSessions;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public double getMinimumAttendancePercentage() {
        return minimumAttendancePercentage;
    }

    public void setMinimumAttendancePercentage(
            double minimumAttendancePercentage) {
        this.minimumAttendancePercentage = minimumAttendancePercentage;
    }

    public boolean isShortage() {
        return shortage;
    }

    public void setShortage(boolean shortage) {
        this.shortage = shortage;
    }
}