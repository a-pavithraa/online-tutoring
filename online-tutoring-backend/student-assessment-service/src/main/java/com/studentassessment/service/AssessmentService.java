package com.studentassessment.service;

import com.studentassessment.api.mdm.MdmClient;
import com.studentassessment.awsservices.AWSUtilityService;
import com.studentassessment.awsservices.SQSUploadListener;
import com.studentassessment.entity.Assessment;
import com.studentassessment.entity.Student;
import com.studentassessment.entity.StudentAssessmentMapping;
import com.studentassessment.entity.StudentAssessmentMappingId;
import com.studentassessment.model.*;
import com.studentassessment.repo.AssessmentRepo;
import com.studentassessment.repo.StudentAssessmentMappingRepo;
import com.studentassessment.repo.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentService {
    private final AssessmentRepo assessmentRepo;
    private final MdmClient mdmClient;

    private final AWSUtilityService awsUtilityService;
    private final StudentRepo studentRepo;
    private final StudentAssessmentMappingRepo studentAssessmentMappingRepo;
    private static final Logger LOG = LoggerFactory.getLogger(AssessmentService.class);

    @Transactional
    public void createAssessment(CreateAssessmentRequest createAssessmentRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime assessmentDate = LocalDateTime.parse(createAssessmentRequest.getAssessmentDate(), formatter);

        Assessment assessment = Assessment.builder()
                .subjectId(createAssessmentRequest.getSubjectId())
                .gradeId(createAssessmentRequest.getGradeId())
                .teacherId(createAssessmentRequest.getTeacherId())
                .assessmentDate(assessmentDate)
                .build();
        assessmentRepo.persist(assessment);
    }



    @Transactional
    public void sendMailToStudentsOnQuestionsUpload(S3EventNotification.S3 s3Entity) {
        S3UploadDocDetailsRecord uploadDocDetailsRecord = awsUtilityService.getSignedUrlAndAssignmentId(s3Entity);
        Assessment assessmentRecord = assessmentRepo.findById(uploadDocDetailsRecord.assignmentId()).orElseThrow();

        List<StudentRecord> students = mdmClient.getStudents(assessmentRecord.getTeacherId(), assessmentRecord.getGradeId(), assessmentRecord.getSubjectId());
        assessmentRecord.setQuestionPaperDocument(s3Entity.getObject().getKey());
        assessmentRepo.persist(assessmentRecord);

        for (StudentRecord studentRecord : students) {
            StringBuilder content = new StringBuilder();
            content.append("Dear ").append(studentRecord.fullName()).append(",\n");
            content.append("Please click on the below url to access the question paper:").append("\n").append(uploadDocDetailsRecord.url());
            awsUtilityService.sendMail(studentRecord.email(), "Question Paper Link", content.toString());

        }

    }

    @Transactional
    public void sendMailToTeacherOnAnswerSheetUpload(S3EventNotification.S3 s3Entity) {
        S3UploadDocDetailsRecord uploadDocDetailsRecord = awsUtilityService.getSignedUrlAndAssignmentId(s3Entity);
        LOG.info("uploaded doc details:{}",uploadDocDetailsRecord);
        TeacherRecord teacherRecord = mdmClient.getTeacherById(uploadDocDetailsRecord.teacherId());
        Assessment assessmentRecord = assessmentRepo.findById(uploadDocDetailsRecord.assignmentId()).orElseThrow();
        Student student = studentRepo.findById(uploadDocDetailsRecord.studentId()).orElseThrow();
        StudentAssessmentMappingId studentAssessmentMappingId = new StudentAssessmentMappingId(uploadDocDetailsRecord.studentId(), uploadDocDetailsRecord.assignmentId());
        //Need to add logic for replacing uploaded document
        StudentAssessmentMapping studentAssessmentMapping = StudentAssessmentMapping.builder()
                .id(studentAssessmentMappingId)
                .assessment(assessmentRecord)
                .student(student)
                .uploadedDocument(s3Entity.getObject().getKey())
                .build();
        studentAssessmentMappingRepo.persist(studentAssessmentMapping);

        StringBuilder content = new StringBuilder();
        content.append("Dear ").append(teacherRecord.name()).append(",\n");
        content.append("Please click on the below url to access the answer sheet uploaded:").append("\n").append(uploadDocDetailsRecord.url());
        awsUtilityService.sendMail(teacherRecord.email(), "Answer sheet uploaded by " + student.getUserName(), content.toString());


    }

    public SearchAssessmentResponse getAssessmentDetails(Long teacherId, Long gradeId, Long subjectId){
        List<AssessmentDetailsRecord> assessmentDetailsRecords= assessmentRepo.getAssessments(teacherId,gradeId,subjectId);
        SearchAssessmentResponse searchAssessmentResponse = new SearchAssessmentResponse(assessmentDetailsRecords);
        return searchAssessmentResponse;
    }




}
