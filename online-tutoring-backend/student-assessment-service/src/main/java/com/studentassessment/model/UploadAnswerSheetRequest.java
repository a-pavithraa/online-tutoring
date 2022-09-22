package com.studentassessment.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class UploadAnswerSheetRequest {
    private long assignmentId;
    private long teacherId;
    private long studentId;
    private MultipartFile answerSheet;
}
