package com.studentassessment.model.assessment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateSubmittedAssessmentRequest {
    private long studentId;
    private long assessmentId;
    private String cognitoStudentId;
    private String studentEmail;
    private MultipartFile correctedDocument;
    private double marks;
}
