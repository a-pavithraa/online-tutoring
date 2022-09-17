package com.studentassessment.controller;

import com.studentassessment.model.CreateAssessmentRequest;
import com.studentassessment.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final S3Client s3Client;
    @Value("${questionpaper.bucket.name}")
    private String bucketName;
    private static final Logger logger = LoggerFactory.getLogger(AssessmentController.class);
    @PostMapping("/assessment")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAssessment(@RequestBody @Valid CreateAssessmentRequest createAssessmentRequest){
        //add validation to check whether any previous assessment is scheduled
        assessmentService.createAssessment(createAssessmentRequest);
    }

    @PostMapping(path="/uploadQuestionPaper",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void uploadQuestionPaper(@RequestParam("assignmentId") long assignmentId, @RequestParam("file") MultipartFile file) throws IOException {
        logger.info("assignment id={}",assignmentId);
        logger.info("getOriginalFilename name={}",file.getOriginalFilename());
        logger.info(" name={}",file.getName());
        Map<String, String> metadata = new HashMap<>();
        metadata.put("x-amz-meta-assignmentid", String.valueOf(assignmentId));
        String fileName = file.getOriginalFilename();
        String extension =fileName.substring(fileName.lastIndexOf("."));
        String newFileName = "QnPaper_"+assignmentId+"_"+ UUID.randomUUID()+extension;
        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(newFileName)
                .metadata(metadata)
                .build();
        byte[]   bytesArray = file.getBytes();
        PutObjectResponse response = s3Client.putObject(putOb, software.amazon.awssdk.core.sync.RequestBody.fromBytes(bytesArray));

    }

}
