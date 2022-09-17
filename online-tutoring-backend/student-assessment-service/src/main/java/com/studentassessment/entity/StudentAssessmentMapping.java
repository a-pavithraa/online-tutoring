package com.studentassessment.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "student_assessment_mapping")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAssessmentMapping {

    @EmbeddedId
    private StudentAssessmentMappingId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("assessmentId")
    private Assessment assessment;


}
