package com.example.attendease.service;

import com.example.attendease.entity.AttendanceRecord;
import com.example.attendease.entity.Session;
import com.example.attendease.entity.Student;
import com.example.attendease.exception.DuplicateAttendanceException;
import com.example.attendease.exception.ResourceNotFoundException;
import com.example.attendease.repository.AttendanceRecordRepository;
import com.example.attendease.repository.SessionRepository;
import com.example.attendease.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final StudentRepository studentRepository;
    private final SessionRepository sessionRepository;

    public AttendanceService(
            AttendanceRecordRepository attendanceRecordRepository,
            StudentRepository studentRepository,
            SessionRepository sessionRepository) {

        this.attendanceRecordRepository = attendanceRecordRepository;
        this.studentRepository = studentRepository;
        this.sessionRepository = sessionRepository;
    }

    public List<AttendanceRecord> getAll() {
        return attendanceRecordRepository.findAll();
    }

    public Optional<AttendanceRecord> getById(Long id) {
        return attendanceRecordRepository.findById(id);
    }

    public AttendanceRecord getByIdOrThrow(Long id) {
        return attendanceRecordRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attendance record not found with id: " + id
                        ));
    }

    public List<AttendanceRecord> getByStudent(Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException(
                    "Student not found with id: " + studentId
            );
        }

        return attendanceRecordRepository.findByStudentId(studentId);
    }

    public List<AttendanceRecord> getBySession(Long sessionId) {

        if (!sessionRepository.existsById(sessionId)) {
            throw new ResourceNotFoundException(
                    "Session not found with id: " + sessionId
            );
        }

        return attendanceRecordRepository.findBySessionId(sessionId);
    }

    @Transactional
    public AttendanceRecord markAttendance(
            String rollNumber,
            Long sessionId,
            Boolean present) {

        // Find student using the roll number entered by the user.
        Student student = studentRepository
                .findByRollNumber(rollNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with roll number: "
                                        + rollNumber
                        ));

        // Find the session.
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Session not found with id: " + sessionId
                        ));

        // Business rule:
        // One attendance record per student per session.
        if (attendanceRecordRepository
                .existsByStudentIdAndSessionId(
                        student.getId(),
                        sessionId)) {

            throw new DuplicateAttendanceException(
                    "Attendance has already been marked for this student and session."
            );
        }

        AttendanceRecord record =
                new AttendanceRecord(
                        student,
                        session,
                        present
                );

        return attendanceRecordRepository.save(record);
    }

    @Transactional
    public AttendanceRecord correctAttendance(
            Long attendanceId,
            Boolean present) {

        AttendanceRecord record =
                getByIdOrThrow(attendanceId);

        record.setPresent(present);

        return attendanceRecordRepository.save(record);
    }

    public void delete(Long id) {

        if (!attendanceRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Attendance record not found with id: " + id
            );
        }

        attendanceRecordRepository.deleteById(id);
    }
}