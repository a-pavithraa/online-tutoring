package com.studentassessment.repo;

import com.studentassessment.entity.Student;
import com.vladmihalcea.spring.repository.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student,Long>, HibernateRepository<Student> {

    Optional<Student> findByCognitoId(String cognitoId);

}
