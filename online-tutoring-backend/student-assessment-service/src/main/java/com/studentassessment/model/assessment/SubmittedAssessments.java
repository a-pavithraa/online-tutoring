package com.studentassessment.model.assessment;

public record SubmittedAssessments(long assessmentId, String assessmentDate, String studentName, String studentMailId, String cognitoId, long studentId, String answerSheet, String correctedAnswerSheet, double marks) {
}
