package com.adminservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adminservice.entity.Student;
import com.vladmihalcea.spring.repository.HibernateRepository;

public interface StudentRepository extends JpaRepository<Student, Long>,HibernateRepository<Student> {

}
