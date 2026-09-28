package com.example.attendease.repository;

import com.example.attendease.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository
        extends JpaRepository<AttendanceRecord, Long> {

    Optional<AttendanceRecord> findByStudentIdAndSessionId(
            Long studentId,
            Long sessionId
    );

    boolean existsByStudentIdAndSessionId(
            Long studentId,
            Long sessionId
    );

    List<AttendanceRecord> findByStudentId(Long studentId);

    List<AttendanceRecord> findBySessionId(Long sessionId);

    List<AttendanceRecord> findByStudentIdAndSessionSubjectId(
            Long studentId,
            Long subjectId
    );
}