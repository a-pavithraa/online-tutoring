package com.studentassessment.service;

import com.studentassessment.api.mdm.MdmClient;
import com.studentassessment.awsservices.S3ActionsService;
import com.studentassessment.entity.Assessment;
import com.studentassessment.model.*;
import com.studentassessment.repo.AssessmentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
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

    private final S3ActionsService s3ActionsService;
    private final MailSender mailSender;
    @Transactional
    public void createAssessment(CreateAssessmentRequest createAssessmentRequest){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime assessmentDate = LocalDateTime.parse(createAssessmentRequest.getAssessmentDate(), formatter);

        Assessment assessment=Assessment.builder()
                .subjectId(createAssessmentRequest.getSubjectId())
                .gradeId(createAssessmentRequest.getGradeId())
                .teacherId(createAssessmentRequest.getTeacherId())
                .assessmentDate(assessmentDate)
                .build();
        assessmentRepo.persist(assessment);
    }

    @Transactional
    public void sendMailToStudentsOnQuestionsUpload(S3EventNotification.S3 s3Entity ){
        S3UploadDocDetailsRecord uploadDocDetailsRecord =s3ActionsService.getSignedUrlAndAssignmentId(s3Entity);
        Assessment assessmentRecord = assessmentRepo.findById(uploadDocDetailsRecord.assignmentId()).orElseThrow();
        List<StudentRecord> students =mdmClient.getStudents(assessmentRecord.getTeacherId(), assessmentRecord.getGradeId(), assessmentRecord.getSubjectId());
        assessmentRecord.setDocumentUrl(uploadDocDetailsRecord.url());
        assessmentRepo.persist(assessmentRecord);
        for(StudentRecord studentRecord:students){
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            StringBuilder content = new StringBuilder();
            content.append("Dear ").append(studentRecord.fullName()).append(",\n");
            content.append("Please click on the below url to access the question paper:").append("\n").append(uploadDocDetailsRecord.url());
            simpleMailMessage.setFrom("a.pavithraa@gmail.com");
            simpleMailMessage.setTo(studentRecord.email());
            simpleMailMessage.setSubject("Question Paper Link");
            simpleMailMessage.setText("Dear ");
            simpleMailMessage.setText(content.toString());
            mailSender.send(simpleMailMessage);
        }

    }






}
