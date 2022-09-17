package com.adminservice.repo.impl;

import com.adminservice.model.StudentRecord;
import com.adminservice.repo.CustomStudentRepository;
import com.vladmihalcea.hibernate.query.ListResultTransformer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class CustomStudentRepositoryImpl implements CustomStudentRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private static final ListResultTransformer transformer = (tuple, aliases) -> {
        int i = 0;
        long id =tuple[i]==null?0: ((Number) tuple[i++]).longValue();

        return new StudentRecord(
                id,
                (String) tuple[i++],
                (String) tuple[i++],
                (String) tuple[i++],
                (String) tuple[i++],
                (String) tuple[i++],
                (String) tuple[i++]
        );
    };

    public List<StudentRecord> getAllStudentsOfTeacher(Long teacherId, Long gradeId, Long subjectId) {
       StringBuilder whereClauseBuilder = new StringBuilder();
       if(gradeId!=null)
           whereClauseBuilder.append(" and tsgm.grade_id  = :gradeId");
       if(subjectId!=null)
           whereClauseBuilder.append(" and tsgm.subject_id  = :subjectId");

        String query ="""
                        SELECT
                                	distinct s.id , s.user_name ,s.full_name , s.email , s.parent_name ,s.phone_no,s.address 
                                from
                                	student s ,
                                	student_subject_map ssm ,
                                	teacher_subject_grade_map tsgm
                                where
                                	s.id = ssm.student_id
                                	and ssm.subject_id = tsgm.subject_id
                                	and s.grade_id = tsgm .grade_id                                	
                                	and tsgm.teacher_id  = :teacherId %s
                        """.formatted(whereClauseBuilder.toString());
        Query nativeQuery=entityManager.createNativeQuery(query).setParameter("teacherId", teacherId);
        if(gradeId!=null)
            nativeQuery.setParameter("gradeId", gradeId);
        if(subjectId!=null)
            nativeQuery.setParameter("subjectId", subjectId);
        List<StudentRecord> studentRecords =nativeQuery
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(transformer).getResultList();
        return studentRecords;
    }


}
