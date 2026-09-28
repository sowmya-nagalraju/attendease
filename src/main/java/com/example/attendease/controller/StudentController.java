package com.example.attendease.controller;

import com.example.attendease.entity.Student;
import com.example.attendease.exception.ResourceNotFoundException;
import com.example.attendease.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @PathVariable Long id) {

        return studentService.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id: " + id
                        ));
    }

    @GetMapping("/roll/{rollNumber}")
    public ResponseEntity<Student> getStudentByRollNumber(
            @PathVariable String rollNumber) {

        return studentService.getByRollNumber(rollNumber)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with roll number: "
                                        + rollNumber
                        ));
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(
            @RequestBody Student student) {

        Student savedStudent =
                studentService.save(student);

        return ResponseEntity.ok(savedStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student student) {

        Student existingStudent =
                studentService.getById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found with id: " + id
                                ));

        student.setId(existingStudent.getId());

        Student updatedStudent =
                studentService.save(student);

        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long id) {

        if (studentService.getById(id).isEmpty()) {
            throw new ResourceNotFoundException(
                    "Student not found with id: " + id
            );
        }

        studentService.delete(id);

        return ResponseEntity.noContent().build();
    }
}