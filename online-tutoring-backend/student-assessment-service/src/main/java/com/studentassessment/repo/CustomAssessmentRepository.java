package com.studentassessment.repo;

import com.studentassessment.model.AssessmentDetailsForStudentNotification;
import com.studentassessment.model.AssessmentDetailsRecord;
import com.studentassessment.model.SearchAssessmentRequest;
import com.studentassessment.model.SubmittedAssessmentsRecord;

import java.util.List;

public interface CustomAssessmentRepository {
     List<AssessmentDetailsRecord> getAssessments(Long teacherId, Long gradeId, Long subjectId);
      List<SubmittedAssessmentsRecord> getAllSubmittedAssessments(long teacherId);

     AssessmentDetailsForStudentNotification getAssessmentDetailsForStudentNotification(long assessmentId);
}
