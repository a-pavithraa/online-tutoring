package com.adminservice.repo;

import com.adminservice.entity.TeacherSubjectGradeId;
import com.adminservice.entity.TeacherSubjectGradeMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherSubjectGradeRepository extends JpaRepository<TeacherSubjectGradeMap, TeacherSubjectGradeId> {
}
