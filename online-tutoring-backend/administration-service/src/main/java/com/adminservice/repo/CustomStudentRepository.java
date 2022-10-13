package com.adminservice.repo;

import com.adminservice.model.StudentDetails;

import java.util.List;

public interface CustomStudentRepository {
    List<StudentDetails> getAllStudentsOfTeacher(Long teacherId, Long gradeId, Long subjectId);
}
