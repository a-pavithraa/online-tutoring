package com.adminservice.repo;

import com.adminservice.model.StudentRecord;
import com.vladmihalcea.hibernate.query.ListResultTransformer;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomStudentRepositoryImpl implements CustomStudentRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<StudentRecord> getAllStudentsOfTeacher(long teacherId) {

        List<StudentRecord> studentRecords = entityManager.createNativeQuery("""
                        SELECT
                                	distinct s.id as id, s.name as name, s.email as email, s.parent_name as parentName,s.grade_id as gradeId
                                from
                                	student s ,
                                	student_subject_map ssm ,
                                	teacher_subject_grade_map tsgm
                                where
                                	s.id = ssm.student_id
                                	and ssm.subject_id = tsgm.subject_id
                                	and s.grade_id = tsgm .grade_id
                                	and tsgm.teacher_id  = :teacherId
                        """)
                .setParameter("teacherId", teacherId)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(
                        (ListResultTransformer) (tuple, aliases) -> {
                            int i = 0;
                            return new StudentRecord(
                                    ((Number) tuple[i++]).longValue(),
                                    (String) tuple[i++],
                                    (String) tuple[i++],
                                    (String) tuple[i++],
                                    ((Number) tuple[i++]).longValue()
                            );
                        }
                ).getResultList();
        return studentRecords;
    }
}
