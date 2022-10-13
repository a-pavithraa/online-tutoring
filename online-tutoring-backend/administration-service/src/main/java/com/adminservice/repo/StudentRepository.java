package com.adminservice.repo;

import com.vladmihalcea.spring.repository.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.adminservice.entity.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface StudentRepository extends JpaRepository<Student, Long>, HibernateRepository<Student>,CustomStudentRepository {
    @Query("select id from Student where name=:name")
    public Optional<Long> findIdByName(@Param("name") String name);




}
