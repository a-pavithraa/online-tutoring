package com.studentassessment.entity.dynamodb;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationDetails {
    private long assessmentId;
    private long teacherId;
    private long studentId;
}
