package com.example.attendease.service;

import com.example.attendease.entity.Subject;
import com.example.attendease.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> getById(Long id) {
        return subjectRepository.findById(id);
    }

    public Optional<Subject> getBySubjectCode(String subjectCode) {
        return subjectRepository.findBySubjectCode(subjectCode);
    }

    public Subject save(Subject subject) {
        return subjectRepository.save(subject);
    }

    public void delete(Long id) {
        subjectRepository.deleteById(id);
    }
}