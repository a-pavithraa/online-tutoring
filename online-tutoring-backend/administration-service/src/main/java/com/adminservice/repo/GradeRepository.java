package com.adminservice.repo;

import com.vladmihalcea.spring.repository.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.adminservice.entity.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {

}
