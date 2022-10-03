package com.studentassessment.repo.impl;

import com.studentassessment.model.AssessmentDetailsRecord;
import com.studentassessment.model.AssessmentRecord;
import com.studentassessment.model.SearchAssessmentRequest;
import com.studentassessment.model.SubmittedAssessmentsRecord;
import com.studentassessment.repo.CustomAssessmentRepository;
import com.vladmihalcea.hibernate.query.ListResultTransformer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

public class CustomAssessmentRepositoryImpl implements CustomAssessmentRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private static final ListResultTransformer transformer = (tuple, aliases) -> {
        int i = 0;

        return new AssessmentDetailsRecord(

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
        return new SubmittedAssessmentsRecord(
                id,
                (String) tuple[i++],
                (String) tuple[i++],
                (String) tuple[i++],
                tuple[i]==null?0: ((Number) tuple[i++]).longValue(),
                (String) tuple[i++],
                (String)tuple[i++],
                tuple[i]==null?0: ((Double) tuple[i++])
        );
    };

    public List<AssessmentDetailsRecord> getAssessments(Long teacherId, Long gradeId, Long subjectId){
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
                """.formatted(whereClauseBuilder.toString());
        Query nativeQuery=entityManager.createNativeQuery(query);
        if(gradeId!=null)
            nativeQuery.setParameter("gradeId",gradeId);
        if(subjectId!=null)
            nativeQuery.setParameter("subjectId", subjectId);
        if(teacherId!=null)
            nativeQuery.setParameter("teacherId",teacherId);
        List<AssessmentDetailsRecord> assessmentDetailsRecords =nativeQuery
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(transformer).getResultList();
        return assessmentDetailsRecords;


    }

    public List<SubmittedAssessmentsRecord> getAllSubmittedAssessments(long teacherId){
        String query = """
                select
                	a.id,
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
                """;

        Query nativeQuery=entityManager.createNativeQuery(query);
        nativeQuery.setParameter("teacherId",teacherId);
        List<SubmittedAssessmentsRecord> assessmentDetailsRecords =nativeQuery
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(submittedAssessmentTransformer).getResultList();
        return assessmentDetailsRecords;

    }
}
