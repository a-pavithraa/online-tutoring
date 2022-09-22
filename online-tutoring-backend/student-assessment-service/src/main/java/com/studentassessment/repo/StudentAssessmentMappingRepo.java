package com.studentassessment.repo;

import com.studentassessment.entity.Assessment;
import com.studentassessment.entity.StudentAssessmentMapping;
import com.vladmihalcea.spring.repository.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentAssessmentMappingRepo extends JpaRepository<StudentAssessmentMapping, Long>, HibernateRepository<StudentAssessmentMapping> {



}
