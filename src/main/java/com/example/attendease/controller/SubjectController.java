package com.example.attendease.controller;

import com.example.attendease.entity.Subject;
import com.example.attendease.exception.ResourceNotFoundException;
import com.example.attendease.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubjectById(
            @PathVariable Long id) {

        return subjectService.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subject not found with id: " + id
                        ));
    }

    @GetMapping("/code/{subjectCode}")
    public ResponseEntity<Subject> getSubjectByCode(
            @PathVariable String subjectCode) {

        return subjectService.getBySubjectCode(subjectCode)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subject not found with code: "
                                        + subjectCode
                        ));
    }

    @PostMapping
    public ResponseEntity<Subject> createSubject(
            @RequestBody Subject subject) {

        Subject savedSubject =
                subjectService.save(subject);

        return ResponseEntity.ok(savedSubject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(
            @PathVariable Long id,
            @RequestBody Subject subject) {

        Subject existingSubject =
                subjectService.getById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Subject not found with id: " + id
                                ));

        subject.setId(existingSubject.getId());

        Subject updatedSubject =
                subjectService.save(subject);

        return ResponseEntity.ok(updatedSubject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(
            @PathVariable Long id) {

        if (subjectService.getById(id).isEmpty()) {
            throw new ResourceNotFoundException(
                    "Subject not found with id: " + id
            );
        }

        subjectService.delete(id);

        return ResponseEntity.noContent().build();
    }
}