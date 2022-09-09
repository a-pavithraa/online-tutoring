package com.adminservice.repo;

import com.adminservice.model.DropdownRecord;
import com.vladmihalcea.spring.repository.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.adminservice.entity.Subject;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SubjectRepository extends JpaRepository<Subject, Long>, HibernateRepository<Subject> {

    public List<Subject> findByIdIn(List<Long> ids);

    @Query("""
            select new com.adminservice.model.DropdownRecord(id, name) from Subject 
            """)
    public List<DropdownRecord> getAllSubjects();

	

}
