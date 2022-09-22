package com.adminservice.repo;

import com.adminservice.model.StudentRecord;
import com.adminservice.model.TeacherRecord;
import com.vladmihalcea.spring.repository.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.adminservice.entity.Teacher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, HibernateRepository<Teacher> {

    public Teacher findByUserName(String name);

    @Query("select id from Teacher where name=:name")
    public Optional<Long> findIdByUserName(@Param("name") String name);

    @Query("""
    select new com.adminservice.model.TeacherRecord(
        t.id as id,
        t.fullName ,
        t.email 
    ) 
    FROM Teacher t
    
    """)
    public List<TeacherRecord> getAllTeachers();


    @Query("""
    select new com.adminservice.model.TeacherRecord(
        t.id as id,
        t.fullName ,
        t.email 
    ) 
    FROM Teacher t
    where id=:id
    
    """)
    public Optional<TeacherRecord> findById(@Param("id") long teacherId);

    @Query("""
    select new com.adminservice.model.TeacherRecord(
        t.id as id,
        t.fullName ,
        t.email 
    ) 
    FROM Teacher t
    where user_name=:name
    
    """)
    public Optional<TeacherRecord> findByName(@Param("name") String name);









}
