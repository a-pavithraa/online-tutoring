package com.adminservice.repo;

import com.vladmihalcea.spring.repository.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.adminservice.entity.Grade;
//https://vladmihalcea.com/best-spring-data-jparepository/#more-24246
public interface GradeRepository extends JpaRepository<Grade, Long>,HibernateRepository<Grade> {

}
