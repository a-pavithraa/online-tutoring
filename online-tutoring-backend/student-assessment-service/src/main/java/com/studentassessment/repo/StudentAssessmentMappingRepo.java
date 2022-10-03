package com.studentassessment.repo;

import com.studentassessment.entity.Assessment;
import com.studentassessment.entity.StudentAssessmentMapping;
import com.vladmihalcea.spring.repository.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

public interface StudentAssessmentMappingRepo extends JpaRepository<StudentAssessmentMapping, Long>, HibernateRepository<StudentAssessmentMapping> {


    @Modifying
    @Query("""
            update StudentAssessmentMapping s set s.correctedDocument=:correctedDocName,s.marks=:marks where id.studentId=:studentId and id.assessmentId=:assessmentId
            """)
    void updateAnswerSheetSubmission(@Param("studentId") long studentId,@Param("assessmentId") long assessmentId,@Param("correctedDocName")String documentName, @Param("marks")double marks);

}
