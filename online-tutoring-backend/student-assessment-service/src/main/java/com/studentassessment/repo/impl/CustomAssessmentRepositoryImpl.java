package com.studentassessment.repo.impl;

import com.studentassessment.model.assessment.AssessmentDetailsForStudentNotification;
import com.studentassessment.model.assessment.AssessmentDetails;
import com.studentassessment.model.assessment.SubmittedAssessments;
import com.studentassessment.repo.CustomAssessmentRepository;
import com.vladmihalcea.hibernate.query.ListResultTransformer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class CustomAssessmentRepositoryImpl implements CustomAssessmentRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private static final ListResultTransformer transformer = (tuple, aliases) -> {
        int i = 0;

        return new AssessmentDetails(

                (String) tuple[i++],
                (String) tuple[i++],
                (String) tuple[i++],
                tuple[i++].toString(),
                (String) tuple[i++],
                tuple[i]==null?0: ((Number) tuple[i++]).longValue()
        );
    };
    private static final ListResultTransformer submittedAssessmentTransformer = (tuple, aliases) -> {
        int i = 0;
        long id =tuple[i]==null?0: ((Number) tuple[i++]).longValue();
        return new SubmittedAssessments(
                id,
                (String) tuple[i++],
                tuple[i++].toString(),
                (String) tuple[i++],
                (String) tuple[i++],
                tuple[i]==null?0: ((Number) tuple[i++]).longValue(),
                (String) tuple[i++],
                (String)tuple[i++],
                tuple[i]==null?0: ((Double) tuple[i++])
        );
    };

    private static final ListResultTransformer studentNotificationAssessmentTransformer = (tuple, aliases) -> {
        int i = 0;
       // long id =tuple[i]==null?0: ((Number) tuple[i++]).longValue();
        return new AssessmentDetailsForStudentNotification(
                ((Number) tuple[i++]).longValue(),
                ((Number) tuple[i++]).longValue(),
                ((Number) tuple[i++]).longValue(),
                (String) tuple[i++],
                (String) tuple[i++]

        );
    };

    public List<AssessmentDetails> getAssessments(Long teacherId, Long gradeId, Long subjectId){
        StringBuilder whereClauseBuilder = new StringBuilder();

        if(gradeId!=null)
            whereClauseBuilder.append(" and a.grade_id  = :gradeId");
        if(subjectId!=null)
            whereClauseBuilder.append(" and a.subject_id  = :subjectId");
        if(teacherId!=null)
            whereClauseBuilder.append(" and a.teacher_id  = :teacherId");
        String query = """
                SELECT
                	t.full_name,
                	s.name subject_name ,
                	g.name grade_name,
                	a.assessment_date,
                	a.question_paper_document,
                	a.id
                from
                	assessment a ,
                	teacher t ,
                	grade g ,
                	subject s
                where
                	a.subject_id = s.id
                	and a.grade_id = g.id
                	and a.teacher_id = t.id %s
                	order by assessment_date desc
                """.formatted(whereClauseBuilder.toString());
        Query nativeQuery=entityManager.createNativeQuery(query);
        if(gradeId!=null)
            nativeQuery.setParameter("gradeId",gradeId);
        if(subjectId!=null)
            nativeQuery.setParameter("subjectId", subjectId);
        if(teacherId!=null)
            nativeQuery.setParameter("teacherId",teacherId);
        List<AssessmentDetails> assessmentDetailsRecords =nativeQuery
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(transformer).getResultList();
        return assessmentDetailsRecords;


    }

    public List<SubmittedAssessments> getAllSubmittedAssessments(long teacherId){
        String query = """
                select
                	a.id,
                	DATE_FORMAT(a.assessment_date,'%d/%m/%y'),
                	s.full_name ,
                	s.email,
                	s.cognito_id,
                	sa.student_id student_id,
                	sa.uploaded_document ,
                	sa.corrected_document ,
                	sa.marks
                from
                	student_assessment_mapping sa,
                	assessment a,
                	student s
                where
                	a.id = sa.assessment_id
                	and s.id = sa.student_id
                	and a.teacher_id =:teacherId
                	order by assessment_date desc
                """;

        Query nativeQuery=entityManager.createNativeQuery(query);
        nativeQuery.setParameter("teacherId",teacherId);
        List<SubmittedAssessments> assessmentDetailsRecords =nativeQuery
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(submittedAssessmentTransformer).getResultList();
        return assessmentDetailsRecords;

    }

    public AssessmentDetailsForStudentNotification getAssessmentDetailsForStudentNotification(long assessmentId){
        String query = """
                SELECT
                	t.id,
                	s.id subject_id,
                	g.id grade_id,
                	s.name subject_name ,              
                	
                	DATE_FORMAT(a.assessment_date,'%d/%m/%y')
                from
                	assessment a ,
                	teacher t ,  
                	grade g ,              	
                	subject s
                where
                	a.subject_id = s.id    
                	and a.grade_id = g.id          	
                	and a.teacher_id = t.id 
                	and a.id =:assessmentId
                """;
        Query nativeQuery=entityManager.createNativeQuery(query);
        nativeQuery.setParameter("assessmentId",assessmentId);
        AssessmentDetailsForStudentNotification assessmentDetailsRecords =(AssessmentDetailsForStudentNotification)nativeQuery
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(studentNotificationAssessmentTransformer).getResultList().get(0);
        return assessmentDetailsRecords;

    }



}
