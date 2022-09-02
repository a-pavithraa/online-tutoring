package com.adminservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adminservice.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long>{

    public Teacher findByName(String name);

}
