package com.example.attendease.service;

import com.example.attendease.entity.AttendanceRecord;
import com.example.attendease.entity.Session;
import com.example.attendease.entity.Student;
import com.example.attendease.entity.Subject;
import com.example.attendease.repository.AttendanceRecordRepository;
import com.example.attendease.repository.SessionRepository;
import com.example.attendease.repository.StudentRepository;
import com.example.attendease.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final SessionRepository sessionRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    public ReportService(
            StudentRepository studentRepository,
            SubjectRepository subjectRepository,
            SessionRepository sessionRepository,
            AttendanceRecordRepository attendanceRecordRepository) {

        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.sessionRepository = sessionRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

    public double calculateAttendancePercentage(
            Long studentId,
            Long subjectId) {

        List<Session> sessions =
                sessionRepository.findBySubjectId(subjectId);

        if (sessions.isEmpty()) {
            return 0.0;
        }

        List<AttendanceRecord> records =
                attendanceRecordRepository
                        .findByStudentIdAndSessionSubjectId(
                                studentId,
                                subjectId
                        );

        long presentCount = records.stream()
                .filter(record -> Boolean.TRUE.equals(record.getPresent()))
                .count();

        return (presentCount * 100.0) / sessions.size();
    }

    public boolean hasAttendanceShortage(
            Long studentId,
            Long subjectId) {

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Subject not found with id: " + subjectId
                        ));

        double percentage =
                calculateAttendancePercentage(
                        studentId,
                        subjectId
                );

        return percentage < subject.getMinimumAttendancePercentage();
    }

    public List<Map<String, Object>> getSubjectAttendanceReport(
            Long subjectId) {

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Subject not found with id: " + subjectId
                        ));

        List<Student> students = studentRepository.findAll();

        List<Map<String, Object>> report = new ArrayList<>();

        for (Student student : students) {

            double percentage =
                    calculateAttendancePercentage(
                            student.getId(),
                            subjectId
                    );

            Map<String, Object> row =
                    new LinkedHashMap<>();

            row.put("studentId", student.getId());
            row.put("rollNumber", student.getRollNumber());
            row.put("studentName", student.getName());
            row.put("subjectCode", subject.getSubjectCode());
            row.put("subjectName", subject.getSubjectName());
            row.put(
                    "minimumAttendancePercentage",
                    subject.getMinimumAttendancePercentage()
            );
            row.put(
                    "attendancePercentage",
                    Math.round(percentage * 100.0) / 100.0
            );
            row.put(
                    "shortage",
                    percentage < subject.getMinimumAttendancePercentage()
            );

            report.add(row);
        }

        return report;
    }

    public List<Map<String, Object>> getShortageStudents(
            Long subjectId) {

        List<Map<String, Object>> fullReport =
                getSubjectAttendanceReport(subjectId);

        return fullReport.stream()
                .filter(row -> Boolean.TRUE.equals(row.get("shortage")))
                .toList();
    }
}