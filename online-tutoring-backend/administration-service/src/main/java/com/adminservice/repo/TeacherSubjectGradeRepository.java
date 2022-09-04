package com.adminservice.repo;

import com.adminservice.entity.Teacher;
import com.adminservice.entity.TeacherSubjectGradeId;
import com.adminservice.entity.TeacherSubjectGradeMap;
import com.adminservice.model.TeacherRecord;
import com.vladmihalcea.spring.repository.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherSubjectGradeRepository extends JpaRepository<TeacherSubjectGradeMap, TeacherSubjectGradeId>, HibernateRepository<TeacherSubjectGradeMap> {

    // Commenting out this implementation since it involves unnecessary left joins
    //public TeacherSubjectGradeMap findBySubjectIdAndGradeId(long subjectId, long gradeId);
   /* @Query("select t.id.teacherId from TeacherSubjectGradeMap t where t.id.subjectId = ?1 and t.id.gradeId=?2")
    public Optional<Long> findBySubjectIdAndGradeId(long subjectId, long gradeId);*/

    @Query("""
    select new com.adminservice.model.TeacherRecord(
        t.id as id,
        t.name as name,
        t.email as email
    ) 
    FROM Teacher t , TeacherSubjectGradeMap tsg  
    WHERE t.id = tsg.id.teacherId  AND tsg.id.subjectId=?1 AND tsg.id.gradeId=?2 
    
    """)
    TeacherRecord getTeacherForGradeAndSubject( long subjectId, long gradeId);
}
