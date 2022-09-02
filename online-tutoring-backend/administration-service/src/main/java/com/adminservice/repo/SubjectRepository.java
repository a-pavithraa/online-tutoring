package com.adminservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adminservice.entity.Subject;

import java.util.List;


public interface SubjectRepository extends JpaRepository<Subject, Long> {

    public List<Subject> findByIdIn(List<Long> ids);
	

}
