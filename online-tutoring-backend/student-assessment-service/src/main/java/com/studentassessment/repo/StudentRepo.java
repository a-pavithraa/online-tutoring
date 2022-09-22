package com.studentassessment.repo;

import com.studentassessment.entity.Student;
import com.vladmihalcea.spring.repository.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student,Long>, HibernateRepository<Student> {
}
