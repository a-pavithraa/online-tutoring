package com.studentassessment.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AssessmentRecord(long teacherId, long subjectId, long gradeId, LocalDateTime assessmentDate) {
}
