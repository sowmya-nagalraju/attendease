package com.example.attendease.repository;

import com.example.attendease.entity.Session;
import com.example.attendease.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findBySubject(Subject subject);

    List<Session> findBySubjectId(Long subjectId);

    List<Session> findBySubject_SubjectCode(String subjectCode);

    Optional<Session> findBySubjectIdAndSessionDate(
            Long subjectId,
            LocalDate sessionDate
    );

    Optional<Session> findBySubject_SubjectCodeAndSessionDate(
            String subjectCode,
            LocalDate sessionDate
    );
}