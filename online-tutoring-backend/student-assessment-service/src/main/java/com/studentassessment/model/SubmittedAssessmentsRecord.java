package com.studentassessment.model;

public record SubmittedAssessmentsRecord(long assessmentId,String assessmentDate, String studentName,String studentMailId,String cognitoId,long studentId,String answerSheet,String correctedAnswerSheet,double marks) {
}
