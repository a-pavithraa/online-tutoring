package com.adminservice.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="teacher_subject_grade_map")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TeacherSubjectGradeMap {
    @EmbeddedId
    private TeacherSubjectGradeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("teacherId")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("subjectId")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("gradeId")
    private Grade grade;

}
