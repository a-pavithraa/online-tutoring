package com.adminservice.repo;

import com.adminservice.entity.Teacher;
import com.adminservice.entity.TeacherSubjectGradeId;
import com.adminservice.entity.TeacherSubjectGradeMap;
import com.adminservice.model.GradeAndSubjectMappingRecord;
import com.adminservice.model.TeacherRecord;
import com.vladmihalcea.spring.repository.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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
                t.userName as name,
                t.email as email
            ) 
            FROM Teacher t , TeacherSubjectGradeMap tsg  
            WHERE t.id = tsg.id.teacherId  AND tsg.id.subjectId=?1 AND tsg.id.gradeId=?2 
                
            """)
    TeacherRecord getTeacherForGradeAndSubject(long subjectId, long gradeId);

    @Query("""
            select new com.adminservice.model.GradeAndSubjectMappingRecord(
            g.id,g.name,s.id,s.name
            ) 
            FROM Subject s , TeacherSubjectGradeMap tsg, Grade g
            WHERE s.id = tsg.id.subjectId and g.id = tsg.id.gradeId AND tsg.id.teacherId=?1 
                
            """)
    List<GradeAndSubjectMappingRecord> getGradeAndSubjectMappingForTeacher(long teacherId);

    @Modifying
    @Query("""   
            delete from  TeacherSubjectGradeMap tsg WHERE  tsg.id.teacherId=?1
             
             """)
    void deleteByTeacherId(long teacherId);

    @Query("""
            select count(id.subjectId)=1
             from TeacherSubjectGradeMap tsg 
            where tsg.id.subjectId=:subjectId and tsg.id.gradeId=:gradeId and tsg.id.teacherId!=:teacherId
            """)
    public boolean checkMappingExistsForGradeAndSubject(@Param("subjectId")long subjectId, @Param("gradeId")long gradeId, @Param("teacherId")long teacherId);
    @Modifying
    @Query(value = """
            insert into teacher_subject_grade_map(teacher_id,grade_id,subject_id)values(:teacherId,:gradeId,:subjectId) 
            """, nativeQuery = true)
    void insertTeacherSubjectGradeMapping(@Param("teacherId") long teacherId, @Param("gradeId") long gradeId, @Param("subjectId") long subjectId);
}
