package com.adminservice.entity;

import lombok.*;

import javax.persistence.*;
//https://vladmihalcea.com/the-best-way-to-map-a-many-to-many-association-with-extra-columns-when-using-jpa-and-hibernate/
@Entity
@Table(name="teacher_subject_grade_map")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
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
