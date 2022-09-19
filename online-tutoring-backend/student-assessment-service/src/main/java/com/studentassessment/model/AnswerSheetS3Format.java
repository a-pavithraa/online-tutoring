package com.studentassessment.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnswerSheetS3Format {
    private long assignmentId;
    private long studentId;
    private long teacherId;
}
