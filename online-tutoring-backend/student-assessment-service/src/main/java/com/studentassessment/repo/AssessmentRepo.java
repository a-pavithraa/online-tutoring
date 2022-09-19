package com.studentassessment.repo;

import com.studentassessment.entity.Assessment;
import com.studentassessment.model.AssessmentRecord;
import com.vladmihalcea.spring.repository.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssessmentRepo extends JpaRepository<Assessment, Long>, HibernateRepository<Assessment> {




}
