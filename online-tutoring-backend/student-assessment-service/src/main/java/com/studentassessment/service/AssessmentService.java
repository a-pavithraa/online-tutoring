package com.studentassessment.service;

import com.studentassessment.api.mdm.MdmClient;
import com.studentassessment.awsservices.AWSUtilityService;
import com.studentassessment.awsservices.DynamoDBService;
import com.studentassessment.entity.Assessment;
import com.studentassessment.entity.Student;
import com.studentassessment.entity.StudentAssessmentMapping;
import com.studentassessment.entity.StudentAssessmentMappingId;
import com.studentassessment.entity.dynamodb.NotificationDetails;
import com.studentassessment.entity.dynamodb.StudentNotification;
import com.studentassessment.model.*;
import com.studentassessment.repo.AssessmentRepo;
import com.studentassessment.repo.StudentAssessmentMappingRepo;
import com.studentassessment.repo.StudentRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class AssessmentService {
    private final AssessmentRepo assessmentRepo;
    private final MdmClient mdmClient;

    private final AWSUtilityService awsUtilityService;
    private final StudentRepo studentRepo;

    private final DynamoDBService dynamoDBService;
    private final StudentAssessmentMappingRepo studentAssessmentMappingRepo;
    private static final Logger LOG = LoggerFactory.getLogger(AssessmentService.class);

    @Value("${questionpaper.bucket.name}")
    private String qnPaperBucketName;

    @Value("${answersheet.bucket.name}")
    private String answerSheetBucketName;

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
    @SneakyThrows
    public void sendMailToStudentsOnQuestionsUpload(S3EventNotification.S3 s3Entity) {
        S3UploadDocDetailsRecord uploadDocDetailsRecord = awsUtilityService.getSignedUrlAndAssessmentId(s3Entity);
        Assessment assessmentRecord = assessmentRepo.findById(uploadDocDetailsRecord.assessmentId()).orElseThrow();
        String preSignedUrl = uploadDocDetailsRecord.url();
        List<StudentRecord> students = mdmClient.getStudents(assessmentRecord.getTeacherId(), assessmentRecord.getGradeId(), assessmentRecord.getSubjectId()).getStudentRecords();


        List<StudentNotification> studentNotifications = new ArrayList<>();
        long epochTimeNowPlusHours = LocalDateTime.now().plusHours(6L).toEpochSecond(ZoneOffset.UTC);
        for (StudentRecord studentRecord : students) {
            StringBuilder content = new StringBuilder();
            content.append("Dear ").append(studentRecord.fullName()).append(",\n");
            content.append("Please click on the below url to access the question paper:").append("\n").append(preSignedUrl);
            awsUtilityService.sendMail(studentRecord.email(), "Question Paper Link", content.toString());

            NotificationDetails notificationDetails = NotificationDetails.builder()
                    .assessmentId(assessmentRecord.getId())
                    .studentId(studentRecord.id())
                    .teacherId(assessmentRecord.getTeacherId()).build();
            StudentNotification studentNotification = StudentNotification.builder().cognitoId(studentRecord.cognitoId()).assessmentId(assessmentRecord.getId())
                    .studentId(studentRecord.id())
                    .teacherId(assessmentRecord.getTeacherId()).ttl(epochTimeNowPlusHours).build();
            studentNotifications.add(studentNotification);

        }
        dynamoDBService.insertStudentNotificationRecords(studentNotifications);


    }

    @Transactional
    public void sendMailToTeacherOnAnswerSheetUpload(S3EventNotification.S3 s3Entity) {
        S3UploadDocDetailsRecord uploadDocDetailsRecord = awsUtilityService.getSignedUrlAndAssessmentId(s3Entity);
        LOG.info("uploaded doc details:{}",uploadDocDetailsRecord);
        TeacherRecord teacherRecord = mdmClient.getTeacherById(uploadDocDetailsRecord.teacherId());
        Assessment assessmentRecord = assessmentRepo.findById(uploadDocDetailsRecord.assessmentId()).orElseThrow();
        Student student = studentRepo.findById(uploadDocDetailsRecord.studentId()).orElseThrow();
        StudentAssessmentMappingId studentAssessmentMappingId = new StudentAssessmentMappingId(uploadDocDetailsRecord.studentId(), uploadDocDetailsRecord.assessmentId());
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

    public void uploadAnswerSheet(UploadAnswerSheetRequest answerSheetRequest)  {

        String fileName = answerSheetRequest.getAnswerSheet().getOriginalFilename();
        String extension =fileName.substring(fileName.lastIndexOf("."));

        Map<String,String> metadataMap = Map.ofEntries(
                Map.entry("x-amz-meta-teacherid", String.valueOf(answerSheetRequest.getTeacherId())),
                Map.entry("x-amz-meta-studentid", String.valueOf(answerSheetRequest.getStudentId())),
                Map.entry("x-amz-meta-assessmentid", String.valueOf(answerSheetRequest.getAssessmentId())),
                Map.entry("x-amz-meta-cognitoid", String.valueOf(answerSheetRequest.getCognitoId()))
        );


        String newFileName = "AnswerSheet_"+answerSheetRequest.getAssessmentId()+"_"+answerSheetRequest.getStudentId()+"_"+ UUID.randomUUID()+extension;
        StringBuilder key= new StringBuilder();
        key.append("Uploads/").append(answerSheetRequest.getCognitoId()).append("/");
        key.append(answerSheetRequest.getTeacherId()).append("/").append(answerSheetRequest.getAssessmentId()).append("/");

        key.append(newFileName);
        awsUtilityService.uploadToBucket(answerSheetBucketName,
                key.toString(),
                metadataMap,
                answerSheetRequest.getAnswerSheet());

    }

    @Transactional
    public void uploadQuestionPaper(MultipartFile file,long assessmentId) {

        String fileName = file.getOriginalFilename();
        String extension = fileName!=null && !fileName.equals("") ?fileName.substring(fileName.lastIndexOf(".")):"";
        String newFileName = "QnPaper_"+assessmentId+"_"+ UUID.randomUUID()+extension;
        Map<String,String> metadataMap = Map.ofEntries( Map.entry("x-amz-meta-assessmentid", String.valueOf(assessmentId)));

       awsUtilityService.uploadToBucket(qnPaperBucketName,
               newFileName,
               metadataMap,
               file);
        Assessment assessmentRecord = assessmentRepo.findById(assessmentId).orElseThrow();
        assessmentRecord.setQuestionPaperDocument(newFileName);
        assessmentRepo.persist(assessmentRecord);

        // sendMailToStudentsOnQuestionsUpload(assessmentId,newFileName);
    }

    public Resource downloadDocument(String documentName,String documentType) {
        if("QuestionPaper".equalsIgnoreCase(documentType))
            return awsUtilityService.downloadFile(qnPaperBucketName,documentName);
        return  awsUtilityService.downloadFile(answerSheetBucketName,documentName);
    }

    public SubmittedAssessmentResponse getSubmittedAssessments(long teacherId){
        List<SubmittedAssessmentsRecord> submittedAssessmentsRecords=assessmentRepo.getAllSubmittedAssessments(teacherId);
        SubmittedAssessmentResponse submittedAssessmentResponse=new SubmittedAssessmentResponse(submittedAssessmentsRecords);
        return submittedAssessmentResponse;
    }

    @Transactional
    public void updateSubmittedAssessment(UpdateSubmittedAssessmentRequest answerSheetRequest){

        String fileName = answerSheetRequest.getCorrectedDocument().getOriginalFilename();
        String extension =fileName.substring(fileName.lastIndexOf("."));

        Map<String,String> metadataMap = Map.ofEntries(
                Map.entry("x-amz-meta-cognitoid", String.valueOf(answerSheetRequest.getCognitoStudentId()))
        );


        String newFileName = "CorrectedSheet_"+"_"+ UUID.randomUUID()+extension;
        StringBuilder key= new StringBuilder();
        key.append(answerSheetRequest.getCognitoStudentId()).append("/").append("CorrectedAnswerSheet/").append(answerSheetRequest.getAssessmentId()).append("/");

        key.append(newFileName);
        awsUtilityService.uploadToBucket(answerSheetBucketName,
                key.toString(),
                metadataMap,
                answerSheetRequest.getCorrectedDocument());
        studentAssessmentMappingRepo.updateAnswerSheetSubmission(answerSheetRequest.getStudentId(), answerSheetRequest.getAssessmentId(),
                key.toString(), answerSheetRequest.getMarks());

    }




}
