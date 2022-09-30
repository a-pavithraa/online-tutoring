package com.studentassessment.repo;

import com.studentassessment.model.AssessmentDetailsRecord;
import com.studentassessment.model.SearchAssessmentRequest;

import java.util.List;

public interface CustomAssessmentRepository {
     List<AssessmentDetailsRecord> getAssessments(Long teacherId, Long gradeId, Long subjectId);
}
