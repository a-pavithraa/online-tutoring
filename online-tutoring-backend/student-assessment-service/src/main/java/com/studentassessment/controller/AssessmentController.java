package com.studentassessment.controller;

import com.studentassessment.model.CreateAssessmentRequest;
import com.studentassessment.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;
    @PostMapping("/assessment")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAssessment(@RequestBody @Valid CreateAssessmentRequest createAssessmentRequest){
        //add validation to check whether any previous assessment is scheduled
        assessmentService.createAssessment(createAssessmentRequest);
    }

}
