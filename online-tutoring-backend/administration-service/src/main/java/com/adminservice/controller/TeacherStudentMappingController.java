package com.adminservice.controller;

import com.adminservice.model.RegisterTeacherMappingRequest;
import com.adminservice.model.TeacherRecord;
import com.adminservice.service.CourseRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TeacherStudentMappingController {

    CourseRegistrationService courseRegistrationService;

    public TeacherStudentMappingController(CourseRegistrationService courseRegistrationService) {
        this.courseRegistrationService = courseRegistrationService;
    }

    @PostMapping("/teacherMapping")
    @ResponseStatus(HttpStatus.CREATED)
    public void mapTeacherToSubject(@RequestBody @Valid RegisterTeacherMappingRequest registerTeacherMappingRequest) {
        courseRegistrationService.mapTeacherToSubjectAndGrade(registerTeacherMappingRequest);

    }

    @GetMapping("/teacher/{subjectId}/{gradeId}")
    public TeacherRecord getTeacherForSubjectAndGrade(@PathVariable("subjectId") long subjectId, @PathVariable("gradeId") long gradeId){
        return courseRegistrationService.getTeacherForSubjectAndGrade(subjectId,gradeId);
    }

}
