package com.example.attendease.service;

import com.example.attendease.entity.Session;
import com.example.attendease.entity.Subject;
import com.example.attendease.exception.ResourceNotFoundException;
import com.example.attendease.repository.SessionRepository;
import com.example.attendease.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SubjectRepository subjectRepository;

    public SessionService(
            SessionRepository sessionRepository,
            SubjectRepository subjectRepository) {

        this.sessionRepository = sessionRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<Session> getAll() {
        return sessionRepository.findAll();
    }

    public Optional<Session> getById(Long id) {
        return sessionRepository.findById(id);
    }

    public Session getByIdOrThrow(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Session not found with id: " + id
                        ));
    }

    public List<Session> getBySubject(Long subjectId) {

        if (!subjectRepository.existsById(subjectId)) {
            throw new ResourceNotFoundException(
                    "Subject not found with id: " + subjectId
            );
        }

        return sessionRepository.findBySubjectId(subjectId);
    }

    public Session create(
            String subjectCode,
            LocalDate sessionDate,
            String topic) {

        Subject subject = subjectRepository
                .findBySubjectCode(subjectCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subject not found with code: "
                                        + subjectCode
                        ));

        if (sessionRepository
                .findBySubject_SubjectCodeAndSessionDate(
                        subjectCode,
                        sessionDate
                )
                .isPresent()) {

            throw new IllegalArgumentException(
                    "A session already exists for subject "
                            + subjectCode
                            + " on "
                            + sessionDate
            );
        }

        Session session = new Session();

        session.setSubject(subject);
        session.setSessionDate(sessionDate);
        session.setTopic(topic);

        return sessionRepository.save(session);
    }

    public Session update(
            Long id,
            Long subjectId,
            LocalDate sessionDate,
            String topic) {

        Session session = getByIdOrThrow(id);

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subject not found with id: "
                                        + subjectId
                        ));

        session.setSubject(subject);
        session.setSessionDate(sessionDate);
        session.setTopic(topic);

        return sessionRepository.save(session);
    }

    public void delete(Long id) {

        if (!sessionRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Session not found with id: " + id
            );
        }

        sessionRepository.deleteById(id);
    }
}