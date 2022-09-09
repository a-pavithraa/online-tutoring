package com.adminservice.repo;

import com.adminservice.model.StudentRecord;
import com.adminservice.util.Utilties;

import java.util.List;

public interface CustomStudentRepository {
    List<StudentRecord> getAllStudentsOfTeacher(Long teacherId, Long gradeId, Long subjectId);
}
