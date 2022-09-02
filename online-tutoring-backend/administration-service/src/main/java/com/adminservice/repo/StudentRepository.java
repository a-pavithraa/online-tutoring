package com.adminservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adminservice.entity.Student;


public interface StudentRepository extends JpaRepository<Student, Long> {

}
