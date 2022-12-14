package com.adminservice.repo;

import com.adminservice.model.Dropdown;
import com.vladmihalcea.spring.repository.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.adminservice.entity.Grade;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//https://vladmihalcea.com/best-spring-data-jparepository/#more-24246
public interface GradeRepository extends JpaRepository<Grade, Long>,HibernateRepository<Grade> {

    @Query("""
            select new com.adminservice.model.Dropdown(id, name) from Grade 
            """)
    List<Dropdown> getAllGrades();
}
