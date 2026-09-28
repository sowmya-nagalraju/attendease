package com.example.attendease.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String subjectCode;

    @Column(nullable = false)
    private String subjectName;

    // Configurable minimum attendance percentage for this subject.
    private Double minimumAttendancePercentage = 75.0;

    @OneToMany(mappedBy = "subject")
    @JsonIgnore
    private List<Session> sessions = new ArrayList<>();

    public Subject() {
    }

    public Subject(String subjectCode, String subjectName,
                   Double minimumAttendancePercentage) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.minimumAttendancePercentage = minimumAttendancePercentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getMinimumAttendancePercentage() {
        return minimumAttendancePercentage;
    }

    public void setMinimumAttendancePercentage(Double minimumAttendancePercentage) {
        this.minimumAttendancePercentage = minimumAttendancePercentage;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}