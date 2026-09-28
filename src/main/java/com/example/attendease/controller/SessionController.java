package com.example.attendease.controller;

import com.example.attendease.entity.Session;
import com.example.attendease.exception.ResourceNotFoundException;
import com.example.attendease.service.SessionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public List<Session> getAllSessions() {
        return sessionService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(
            @PathVariable Long id) {

        return sessionService.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Session not found with id: " + id
                        ));
    }

    @GetMapping("/subject/{subjectId}")
    public List<Session> getSessionsBySubject(
            @PathVariable Long subjectId) {

        return sessionService.getBySubject(subjectId);
    }

    @PostMapping
    public ResponseEntity<Session> createSession(
            @RequestParam("subjectCode") String subjectCode,

            @RequestParam("sessionDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate sessionDate,

            @RequestParam(value = "topic", required = false)
            String topic) {

        Session session =
                sessionService.create(
                        subjectCode,
                        sessionDate,
                        topic
                );

        return ResponseEntity.ok(session);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(
            @PathVariable Long id,
            @RequestParam Long subjectId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate sessionDate,

            @RequestParam(required = false) String topic) {

        Session updatedSession =
                sessionService.update(
                        id,
                        subjectId,
                        sessionDate,
                        topic
                );

        return ResponseEntity.ok(updatedSession);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable Long id) {

        if (sessionService.getById(id).isEmpty()) {
            throw new ResourceNotFoundException(
                    "Session not found with id: " + id
            );
        }

        sessionService.delete(id);

        return ResponseEntity.noContent().build();
    }
}