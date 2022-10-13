package com.studentassessment.repo;

import com.studentassessment.entity.Assessment;
import com.vladmihalcea.spring.repository.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepo extends JpaRepository<Assessment, Long>, HibernateRepository<Assessment>,CustomAssessmentRepository {




}
