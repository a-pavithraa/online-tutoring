package com.studentassessment.service;

import com.studentassessment.entity.Assessment;
import com.studentassessment.model.CreateAssessmentRequest;
import com.studentassessment.repo.AssessmentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AssessmentService {
    private final AssessmentRepo assessmentRepo;
    @Transactional
    public void createAssessment(CreateAssessmentRequest createAssessmentRequest){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime assessmentDate = LocalDateTime.parse(createAssessmentRequest.getAssessmentDate(), formatter);

        Assessment assessment=Assessment.builder()
                .subjectId(createAssessmentRequest.getSubjectId())
                .gradeId(createAssessmentRequest.getGradeId())
                .assessmentDate(assessmentDate)
                .build();
        assessmentRepo.persist(assessment);
    }



}
