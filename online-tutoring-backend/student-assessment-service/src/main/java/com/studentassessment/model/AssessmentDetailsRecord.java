package com.studentassessment.model;

import java.time.LocalDateTime;

public record AssessmentDetailsRecord(String teacher, String subject, String grade, String assessmentDate,String qnPaperDocument, long assessmentId) {
}
