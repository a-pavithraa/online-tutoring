package com.studentassessment.repo;

import com.studentassessment.entity.Assessment;
import com.vladmihalcea.spring.repository.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentRepo extends JpaRepository<Assessment, Long>, HibernateRepository<Assessment> {
    List<Assessment> getById(long id);
}
