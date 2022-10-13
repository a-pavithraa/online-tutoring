package com.studentassessment.model.assessment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
public class UploadAnswerSheetRequest {

    private long assessmentId;

    private long teacherId;

    private long studentId;

    private String cognitoId;

    private MultipartFile answerSheet;
}
