package com.studentassessment.controller;

import com.studentassessment.model.*;
import com.studentassessment.service.AssessmentService;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    private static final Logger logger = LoggerFactory.getLogger(AssessmentController.class);

    @PostMapping("/assessment")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAssessment(@RequestBody @Valid CreateAssessmentRequest createAssessmentRequest) {
        //add validation to check whether any previous assessment is scheduled
        assessmentService.createAssessment(createAssessmentRequest);
    }

    @PostMapping(path = "/answerSheetUpload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void uploadAnswerSheet(@ModelAttribute UploadAnswerSheetRequest answerSheetRequest) throws IOException {
        assessmentService.uploadAnswerSheet(answerSheetRequest);
        // Not adding update table call here since upload can be done via front end code also
    }

    @PostMapping(path = "/questionPaperUpload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void uploadQuestionPaper(@RequestParam("assessmentId") long assessmentId, @RequestParam("file") MultipartFile file) throws IOException {
        logger.info("assignment id={}", assessmentId);
        logger.info("getOriginalFilename name={}", file.getOriginalFilename());
        logger.info(" name={}", file.getName());
        assessmentService.uploadQuestionPaper(file, assessmentId);


    }

    @GetMapping(path = "/assessmentDetails")
    public SearchAssessmentResponse getAssessmentDetails(@RequestParam("teacherId") Long teacherId, @RequestParam(required = false) Long gradeId, @RequestParam(required = false) Long subjectId) {
        return assessmentService.getAssessmentDetails(teacherId, gradeId, subjectId);
    }
    @GetMapping(path = "/downloadFile")
    public ResponseEntity<Resource> downloadFile(@RequestParam("documentName") String fileName, @RequestParam("documentType") String fileType) throws IOException{

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .header(HttpHeaders.CONTENT_TYPE,"application/octet-stream")
                .body(assessmentService.downloadDocument(fileName,fileType));


    }
    @GetMapping(path = "/submittedAssessmentDetails")
    public SubmittedAssessmentResponse getSubmittedAssessments(@RequestParam("teacherId") Long teacherId) {
        return assessmentService.getSubmittedAssessments(teacherId);
    }

    @PostMapping(path="/updateSubmittedAssessment")
    public void updateSubmittedAssessment(@ModelAttribute UpdateSubmittedAssessmentRequest updateSubmittedAssessmentRequest){
        assessmentService.updateSubmittedAssessment(updateSubmittedAssessmentRequest);
    }



}
