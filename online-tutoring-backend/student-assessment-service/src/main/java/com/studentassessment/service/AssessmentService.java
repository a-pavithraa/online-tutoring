package com.studentassessment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentassessment.api.mdm.MdmClient;
import com.studentassessment.awsservices.AWSUtilityService;
import com.studentassessment.entity.Assessment;
import com.studentassessment.entity.Student;
import com.studentassessment.entity.StudentAssessmentMapping;
import com.studentassessment.entity.StudentAssessmentMappingId;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public void sendMailToStudentsOnQuestionsUpload(long assessmentId,String key) {
        String preSignedUrl = awsUtilityService.getPresignedUrl(qnPaperBucketName,key);
        Assessment assessmentRecord = assessmentRepo.findById(assessmentId).orElseThrow();

        List<StudentRecord> students = mdmClient.getStudents(assessmentRecord.getTeacherId(), assessmentRecord.getGradeId(), assessmentRecord.getSubjectId());
        assessmentRecord.setQuestionPaperDocument(key);
        assessmentRepo.persist(assessmentRecord);
       /* ObjectMapper mapper = new ObjectMapper();
        String assessmentStr = mapper.writeValueAsString("""
                {
                "assignmentId":%1$s,
                "teacherId":%2$s
                }
                """).formatted(assessmentRecord.getId(),assessmentRecord.getTeacherId());*/

        for (StudentRecord studentRecord : students) {
            StringBuilder content = new StringBuilder();
            content.append("Dear ").append(studentRecord.fullName()).append(",\n");
            content.append("Please click on the below url to access the question paper:").append("\n").append(preSignedUrl);
            awsUtilityService.sendMail(studentRecord.email(), "Question Paper Link", content.toString());
          //  awsUtilityService.uploadJSONToBucket(answerSheetBucketName,"notification/"+studentRecord.id(),assessmentStr);

        }

    }

    @Transactional
    public void sendMailToTeacherOnAnswerSheetUpload(S3EventNotification.S3 s3Entity) {
        S3UploadDocDetailsRecord uploadDocDetailsRecord = awsUtilityService.getSignedUrlAndAssignmentId(s3Entity);
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
        String extension =fileName.substring(fileName.lastIndexOf("."));
        String newFileName = "QnPaper_"+assessmentId+"_"+ UUID.randomUUID()+extension;
        Map<String,String> metadataMap = Map.ofEntries( Map.entry("x-amz-meta-assignmentid", String.valueOf(assessmentId)));

       awsUtilityService.uploadToBucket(qnPaperBucketName,
               newFileName,
               metadataMap,
               file);
        sendMailToStudentsOnQuestionsUpload(assessmentId,newFileName);
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
