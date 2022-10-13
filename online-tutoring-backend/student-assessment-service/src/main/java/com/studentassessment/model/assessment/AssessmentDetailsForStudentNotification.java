package com.studentassessment.model.assessment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public record AssessmentDetailsForStudentNotification(long teacherId,long subjectId, long gradeId,String subject, String assessmentDate) {

}
