package com.studentassessment.service;

import com.studentassessment.api.mdm.MdmClient;
import com.studentassessment.awsservices.AWSUtilityService;
import com.studentassessment.awsservices.DynamoDBService;
import com.studentassessment.entity.Assessment;
import com.studentassessment.entity.Student;
import com.studentassessment.entity.StudentAssessmentMapping;
import com.studentassessment.entity.StudentAssessmentMappingId;
import com.studentassessment.entity.dynamodb.StudentNotification;
import com.studentassessment.model.assessment.*;
import com.studentassessment.model.mdm.StudentDetails;
import com.studentassessment.model.mdm.TeacherDetails;
import com.studentassessment.model.s3.S3EventNotification;
import com.studentassessment.model.s3.S3UploadDocDetails;
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
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.studentassessment.api.mdm.Constants.Metadata;

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

    private final static String S3_METADATA_PREFIX = "x-amz-meta-";

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

    public void processQuestionPaperUpload(S3EventNotification.S3 s3Entity) {

    }

    public void sendMailToStudents(StudentDetails studentRecord, String preSignedUrl) {
        StringBuilder content = new StringBuilder();
        content.append("Dear ").append(studentRecord.fullName()).append(",\n");
        content.append("Please click on the below url to access the question paper:").append("\n").append(preSignedUrl);
        awsUtilityService.sendMail(studentRecord.email(), "Question Paper Link", content.toString());

    }


    @Transactional
    @SneakyThrows
    public void processQuestionPaperUpload(S3UploadDocDetails uploadDocDetailsRecord, String preSignedUrl) {

        long assessmentId = uploadDocDetailsRecord.assessmentId();
        AssessmentDetailsForStudentNotification assessmentRecord = assessmentRepo
                .getAssessmentDetailsForStudentNotification(assessmentId);
        List<StudentDetails> students = mdmClient.getStudents(assessmentRecord.teacherId(), assessmentRecord.gradeId(),
                assessmentRecord.subjectId()).getStudentRecords();

        List<StudentNotification> studentNotifications = new ArrayList<>();
        long ttlForDynamoDbItem = LocalDateTime.now().plusHours(6L).toEpochSecond(ZoneOffset.UTC);
        for (StudentDetails studentRecord : students) {

            sendMailToStudents(studentRecord, preSignedUrl);

            StudentNotification studentNotification = StudentNotification.builder().cognitoId(studentRecord.cognitoId()).assessmentId(assessmentId)
                    .studentId(studentRecord.id())
                    .subject(assessmentRecord.subject())
                    .dueDate(assessmentRecord.assessmentDate())
                    .teacherId(assessmentRecord.teacherId()).ttl(ttlForDynamoDbItem).build();
            studentNotifications.add(studentNotification);

        }
        if (studentNotifications.size() > 0)
            dynamoDBService.insertStudentNotificationRecords(studentNotifications);


    }

    @Transactional
    public void saveSubmittedAssessmentDetails(S3UploadDocDetails uploadDocDetailsRecord, TeacherDetails teacherRecord, Student student) {
        Assessment assessmentRecord = assessmentRepo.findById(uploadDocDetailsRecord.assessmentId()).orElseThrow();
        StudentAssessmentMappingId studentAssessmentMappingId = new StudentAssessmentMappingId(uploadDocDetailsRecord.studentId(), uploadDocDetailsRecord.assessmentId());
        //Need to add logic for replacing uploaded document
        StudentAssessmentMapping studentAssessmentMapping = StudentAssessmentMapping.builder()
                .id(studentAssessmentMappingId)
                .assessment(assessmentRecord)
                .student(student)
                .uploadedDocument(uploadDocDetailsRecord.key())
                .build();
        studentAssessmentMappingRepo.persist(studentAssessmentMapping);

    }

    @Transactional
    public void processAnswerSheetUpload(S3UploadDocDetails uploadDocDetailsRecord, String presignedUrl) {

        LOG.info("uploaded doc details:{}", uploadDocDetailsRecord);
        TeacherDetails teacherRecord = mdmClient.getTeacherById(uploadDocDetailsRecord.teacherId());
        Student student = studentRepo.findByCognitoId(uploadDocDetailsRecord.cognitoId()).orElseThrow();
        saveSubmittedAssessmentDetails(uploadDocDetailsRecord, teacherRecord, student);


        StringBuilder content = new StringBuilder();
        content.append("Dear ").append(teacherRecord.name()).append(",\n");
        content.append("Please click on the below url to access the answer sheet uploaded:").append("\n").append(presignedUrl);
        awsUtilityService.sendMail(teacherRecord.email(), "Answer sheet uploaded by " + student.getUserName(), content.toString());


    }

    public SearchAssessmentResponse getAssessmentDetails(Long teacherId, Long gradeId, Long subjectId) {
        List<AssessmentDetails> assessmentDetailsRecords = assessmentRepo.getAssessments(teacherId, gradeId, subjectId);
        SearchAssessmentResponse searchAssessmentResponse = new SearchAssessmentResponse(assessmentDetailsRecords);
        return searchAssessmentResponse;
    }

    //This method is not used currently. Just for internal testing purpose
    public void uploadAnswerSheet(UploadAnswerSheetRequest answerSheetRequest) {

        String fileName = answerSheetRequest.getAnswerSheet().getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));

        Map<String, String> metadataMap = Map.ofEntries(
                Map.entry(S3_METADATA_PREFIX.concat(Metadata.TEACHER_ID.toString()), String.valueOf(answerSheetRequest.getTeacherId())),
                Map.entry(S3_METADATA_PREFIX.concat(Metadata.STUDENT_ID.toString()), String.valueOf(answerSheetRequest.getStudentId())),
                Map.entry(S3_METADATA_PREFIX.concat(Metadata.ASSESSMENT_ID.toString()), String.valueOf(answerSheetRequest.getAssessmentId())),
                Map.entry(S3_METADATA_PREFIX.concat(Metadata.COGNITO_ID.toString()), String.valueOf(answerSheetRequest.getCognitoId()))
        );


        String newFileName = "AnswerSheet_" + answerSheetRequest.getAssessmentId() + "_" + UUID.randomUUID() + extension;
        StringBuilder key = new StringBuilder();
        key.append("Uploads/").append(answerSheetRequest.getCognitoId()).append("/");
        key.append(answerSheetRequest.getTeacherId()).append("/").append(answerSheetRequest.getAssessmentId()).append("/");

        key.append(newFileName);
        awsUtilityService.uploadToBucket(answerSheetBucketName,
                key.toString(),
                metadataMap,
                answerSheetRequest.getAnswerSheet());

    }

    @Transactional
    public void uploadQuestionPaper(MultipartFile file, long assessmentId) {

        String fileName = file.getOriginalFilename();
        String extension = fileName != null && !fileName.equals("") ? fileName.substring(fileName.lastIndexOf(".")) : "";
        StringBuilder newFileName = new StringBuilder("QnPaper_");
        newFileName.append(assessmentId).append("_").append(UUID.randomUUID()).append(extension);


        Map<String, String> metadataMap = Map.ofEntries(Map.entry(S3_METADATA_PREFIX.concat(Metadata.ASSESSMENT_ID.toString()), String.valueOf(assessmentId)));

        awsUtilityService.uploadToBucket(qnPaperBucketName,
                newFileName.toString(),
                metadataMap,
                file);
        Assessment assessmentRecord = assessmentRepo.findById(assessmentId).orElseThrow();
        assessmentRecord.setQuestionPaperDocument(newFileName.toString());
        assessmentRepo.persist(assessmentRecord);

        // sendMailToStudentsOnQuestionsUpload(assessmentId,newFileName);
    }

    public Resource downloadDocument(String documentName, String documentType) {
        if ("QuestionPaper".equalsIgnoreCase(documentType))
            return awsUtilityService.downloadFile(qnPaperBucketName, documentName);
        return awsUtilityService.downloadFile(answerSheetBucketName, documentName);
    }

    public SubmittedAssessmentResponse getSubmittedAssessments(long teacherId) {
        List<SubmittedAssessments> submittedAssessmentsRecords = assessmentRepo.getAllSubmittedAssessments(teacherId);
        SubmittedAssessmentResponse submittedAssessmentResponse = new SubmittedAssessmentResponse(submittedAssessmentsRecords);
        return submittedAssessmentResponse;
    }

    // TODO: 12-10-2022 Cognito User Pool ID can't be used. This bug needs to be fixed. Need to modify to identity pool id
    @Transactional
    public void updateSubmittedAssessment(UpdateSubmittedAssessmentRequest answerSheetRequest) {

        String fileName = answerSheetRequest.getCorrectedDocument().getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));


        Map<String, String> metadataMap = Map.ofEntries(
                Map.entry(S3_METADATA_PREFIX.concat(Metadata.COGNITO_ID.toString()), String.valueOf(answerSheetRequest.getCognitoStudentId()))
        );
        StringBuilder newFileName = new StringBuilder("CorrectedSheet_")
                .append(UUID.randomUUID()).append(extension);

        StringBuilder key = new StringBuilder();
        key.append(answerSheetRequest.getCognitoStudentId()).append("/").append("CorrectedAnswerSheet/").append(answerSheetRequest.getAssessmentId()).append("/");

        key.append(newFileName.toString());
        awsUtilityService.uploadToBucket(answerSheetBucketName,
                key.toString(),
                metadataMap,
                answerSheetRequest.getCorrectedDocument());
        studentAssessmentMappingRepo.updateAnswerSheetSubmission(answerSheetRequest.getStudentId(), answerSheetRequest.getAssessmentId(),
                key.toString(), answerSheetRequest.getMarks());

    }

    public StudentPerformanceResponse getStudentPerformance(long studentId){

        List<StudentPerformance> studentPerformanceList = studentAssessmentMappingRepo.getPerformanceDetails(studentId);
        StudentPerformanceResponse studentPerformanceResponse = new StudentPerformanceResponse(studentPerformanceList);
        return studentPerformanceResponse;
    }


}
