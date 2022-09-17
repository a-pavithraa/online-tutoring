package com.studentassessment.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAssessmentMappingId implements Serializable {
    private Long assessmentId;
    private Long studentId;
}
