package com.studentassessment.repo;

import com.studentassessment.model.assessment.AssessmentDetailsForStudentNotification;
import com.studentassessment.model.assessment.AssessmentDetails;
import com.studentassessment.model.assessment.SubmittedAssessments;

import java.util.List;

public interface CustomAssessmentRepository {
     List<AssessmentDetails> getAssessments(Long teacherId, Long gradeId, Long subjectId);
      List<SubmittedAssessments> getAllSubmittedAssessments(long teacherId);

     AssessmentDetailsForStudentNotification getAssessmentDetailsForStudentNotification(long assessmentId);
}
