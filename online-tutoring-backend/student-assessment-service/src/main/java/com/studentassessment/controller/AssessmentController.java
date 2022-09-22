package com.studentassessment.controller;

import com.studentassessment.model.CreateAssessmentRequest;
import com.studentassessment.model.UploadAnswerSheetRequest;
import com.studentassessment.service.AssessmentService;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assessment")
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final S3Template s3Template;
    @Value("${questionpaper.bucket.name}")
    private String qnPaperBucketName;

    @Value("${answersheet.bucket.name}")
    private String answerSheetBucketName;
    private static final Logger logger = LoggerFactory.getLogger(AssessmentController.class);
    @PostMapping("/assessment")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAssessment(@RequestBody @Valid CreateAssessmentRequest createAssessmentRequest){
        //add validation to check whether any previous assessment is scheduled
        assessmentService.createAssessment(createAssessmentRequest);
    }

    @PostMapping(path="/answerSheetUpload",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void uploadAnswerSheet(@ModelAttribute UploadAnswerSheetRequest answerSheetRequest) throws IOException{
        String fileName = answerSheetRequest.getAnswerSheet().getOriginalFilename();
        String extension =fileName.substring(fileName.lastIndexOf("."));
        ObjectMetadata objectMetadata=ObjectMetadata.builder()
                .metadata("x-amz-meta-teacherid", String.valueOf(answerSheetRequest.getTeacherId()))
                .metadata("x-amz-meta-studentid", String.valueOf(answerSheetRequest.getStudentId()))
                .metadata("x-amz-meta-assignmentid", String.valueOf(answerSheetRequest.getAssignmentId()))
                .build();

        String newFileName = "AnswerSheet_"+answerSheetRequest.getAssignmentId()+"_"+answerSheetRequest.getStudentId()+"_"+ UUID.randomUUID()+extension;
        StringBuilder key= new StringBuilder();
        key.append(answerSheetRequest.getTeacherId()).append("/").append(answerSheetRequest.getAssignmentId()).append("/");
        key.append(answerSheetRequest.getStudentId()).append("/");
        key.append(newFileName);
        s3Template.upload(answerSheetBucketName, key.toString(), answerSheetRequest.getAnswerSheet().getInputStream(),objectMetadata);
    // Not adding update table call here since upload can be done via front end code also
    }

    @PostMapping(path="/questionPaperUpload",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void uploadQuestionPaper(@RequestParam("assignmentId") long assignmentId, @RequestParam("file") MultipartFile file) throws IOException {
        logger.info("assignment id={}",assignmentId);
        logger.info("getOriginalFilename name={}",file.getOriginalFilename());
        logger.info(" name={}",file.getName());

        String fileName = file.getOriginalFilename();
        String extension =fileName.substring(fileName.lastIndexOf("."));
        String newFileName = "QnPaper_"+assignmentId+"_"+ UUID.randomUUID()+extension;

        ObjectMetadata objectMetadata=ObjectMetadata.builder().metadata("x-amz-meta-assignmentid", String.valueOf(assignmentId)).build();
        s3Template.upload(qnPaperBucketName,newFileName,file.getInputStream(),objectMetadata);


    }



}
